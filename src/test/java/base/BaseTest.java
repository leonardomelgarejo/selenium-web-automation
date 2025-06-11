package base;

import io.github.bonigarcia.wdm.WebDriverManager;
import io.qameta.allure.Allure;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.extension.*;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.TakesScreenshot;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;

import java.io.ByteArrayInputStream;

public abstract class BaseTest {
    protected WebDriver driver;

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
    public void setUp() {
        WebDriverManager.chromedriver().setup();
        driver = new ChromeDriver();
        driver.manage().window().maximize();
    }

    @AfterEach
    public void tearDown() {
        if (driver != null) {
            driver.quit();
        }
    }
}