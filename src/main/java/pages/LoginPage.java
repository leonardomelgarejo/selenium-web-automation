package pages;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;

public class LoginPage {
    private WebDriver driver;

    private By usernameInput = By.cssSelector("[data-test='username']");
    private By passwordInput = By.cssSelector("[data-test='password']");
    private By loginButton   = By.cssSelector("[data-test='login-button']");

    private By loginError = By.cssSelector("h3[data-test='error']");

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

    public LoginPage clickLoginShouldFailWhenUserIsInvalid() {
        driver.findElement(loginButton).click();
        return new LoginPage(driver);
    }

    public HomePage clickLogin() {
        driver.findElement(loginButton).click();
        return new HomePage(driver);
    }

    public String getLoginErrorMessage(){
        WebElement errorContainer = driver.findElement(loginError);
        String fullText = errorContainer.getText();
        String loginErrorMessage = fullText.split(":", 2)[1].trim();

        return loginErrorMessage;
    }
}