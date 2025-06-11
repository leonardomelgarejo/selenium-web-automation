package pages;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;

public class HomePage {
    private WebDriver driver;

    private By homePageLogo = By.cssSelector(".app_logo");

    public HomePage(WebDriver driver) {
        this.driver = driver;
    }

    public boolean isHomePageLogo() {
        return driver.findElement(homePageLogo).isDisplayed();
    }
}