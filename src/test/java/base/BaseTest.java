package base;

import base.factory.ChromeOptionsFactory;
import config.Config;
import io.github.bonigarcia.wdm.WebDriverManager;
import io.qameta.allure.Allure;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
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
import java.time.Duration;
import java.util.Properties;

public abstract class BaseTest {
    protected WebDriver driver;
    private static boolean environmentWritten = false;

    @BeforeAll
    static void globalSetup() {
        WebDriverManager.chromedriver().setup();
    }

    @BeforeAll
    static void writeAllureEnvironment() throws Exception {
        if (environmentWritten) {
            return;
        }
        var driverManager = WebDriverManager.chromedriver();
        driverManager.setup();
        String chromeDriverVersion = driverManager.getDownloadedDriverVersion();

        Properties props = new Properties();
        props.setProperty("Browser", "Chrome");
        props.setProperty("ChromeDriver.Version", chromeDriverVersion);
        props.setProperty("BaseURL", Config.BASE_URL);
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
                byte[] img = ((TakesScreenshot) driver).getScreenshotAs(OutputType.BYTES);
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
        ChromeOptions options = ChromeOptionsFactory.build();
        driver = new ChromeDriver(options);
        driver.manage().window().maximize();

        driver.manage()
                .timeouts()
                .implicitlyWait(Duration.ofSeconds(Config.IMPLICIT_WAIT));
    }

    @AfterEach
    public void tearDown() {
        if (driver != null) {
            driver.quit();
        }
    }
}
