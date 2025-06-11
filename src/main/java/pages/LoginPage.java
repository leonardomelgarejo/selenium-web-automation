package pages;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;

public class LoginPage {
    private WebDriver driver;

    private By usernameInput = By.cssSelector("[data-test='username']");
    private By passwordInput = By.cssSelector("[data-test='password']");
    private By loginButton   = By.cssSelector("[data-test='login-button']");

    public LoginPage(WebDriver driver) {
        this.driver = driver;
    }

    public LoginPage enterUsername(String username) {
        driver.findElement(usernameInput).sendKeys(username);
        return this;
    }

    public LoginPage enterPassword(String password) {
        driver.findElement(passwordInput).sendKeys(password);
        return this;
    }

    public HomePage clickLogin() {
        driver.findElement(loginButton).click();
        return new HomePage(driver);
    }
}