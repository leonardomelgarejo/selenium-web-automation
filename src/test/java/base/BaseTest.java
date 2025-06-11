package base;

import io.github.bonigarcia.wdm.WebDriverManager;
import io.qameta.allure.Allure;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.extension.AfterTestExecutionCallback;
import org.junit.jupiter.api.extension.RegisterExtension;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.TakesScreenshot;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;

import java.io.ByteArrayInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Properties;

public abstract class BaseTest {
    protected WebDriver driver;
    private static boolean environmentWritten = false;

    /**
     * Gera o arquivo environment.properties para o Allure, apenas uma vez.
     */
    @BeforeAll
    static void writeAllureEnvironment() throws Exception {
        if (environmentWritten) {
            return;
        }
        // Garante que o ChromeDriver foi baixado e obtém versão
        var driverManager = WebDriverManager.chromedriver();
        driverManager.setup();
        String chromeDriverVersion = driverManager.getDownloadedDriverVersion();

        Properties props = new Properties();
        props.setProperty("Browser", "Chrome");
        props.setProperty("ChromeDriver.Version", chromeDriverVersion);
        props.setProperty("BaseURL", System.getProperty("app.url", "https://www.saucedemo.com"));
        props.setProperty("OS", System.getProperty("os.name"));
        props.setProperty("OS.Version", System.getProperty("os.version"));
        props.setProperty("Java.Version", System.getProperty("java.version"));

        Path results = Path.of("target", "allure-results");
        Files.createDirectories(results);
        try (OutputStream os = new FileOutputStream(results.resolve("environment.properties").toFile())) {
            props.store(os, "Allure environment properties");
        }
        environmentWritten = true;
    }

    @RegisterExtension
    static AfterTestExecutionCallback screenshotAlways = context -> {
        Object testInstance = context.getRequiredTestInstance();
        if (testInstance instanceof BaseTest) {
            WebDriver driver = ((BaseTest) testInstance).driver;
            if (driver instanceof TakesScreenshot) {
                byte[] img = ((TakesScreenshot) driver)
                        .getScreenshotAs(OutputType.BYTES);
                String status = context.getExecutionException().isPresent() ? "FAIL" : "PASS";
                Allure.getLifecycle()
                        .addAttachment(
                                "Screenshot [" + status + "]",
                                "image/png",
                                "png",
                                new ByteArrayInputStream(img)
                        );
            }
        }
    };

    @BeforeEach
    public void setUp() throws IOException {
        // Configura e inicia o WebDriver com ChromeOptions para CI/Linux
        WebDriverManager.chromedriver().setup();
        ChromeOptions options = new ChromeOptions();
        options.addArguments("--headless=new");
        options.addArguments("--no-sandbox");
        options.addArguments("--disable-dev-shm-usage");
        options.addArguments("--remote-allow-origins=*");
        String profileDir = Files.createTempDirectory("chrome-profile").toString();
        options.addArguments("--user-data-dir=" + profileDir);

        driver = new ChromeDriver(options);
        driver.manage().window().maximize();
    }

    @AfterEach
    public void tearDown() {
        if (driver != null) {
            driver.quit();
        }
    }
}
