package tests;

import base.BaseTest;
import config.Config;
import io.qameta.allure.Feature;
import org.junit.jupiter.api.Test;
import pages.LoginPage;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

@Feature("Login")
public class LoginTest extends BaseTest {

    @Test
    public void shouldLoginSuccessfully() {
        driver.get(Config.BASE_URL);

        boolean loggedIn = new LoginPage(driver)
                .enterUsername(Config.USERNAME)
                .enterPassword(Config.PASSWORD)
                .clickLogin()
                .isHomePageLogo();

        assertTrue(loggedIn, "Deveria exibir a homepage");
    }

    @Test
    public void shouldNotLoginWithInvalidUser() {
        driver.get(Config.BASE_URL);

        String loginErrorMessage = new LoginPage(driver)
                .enterUsername("invalid_user")
                .enterPassword(Config.PASSWORD)
                .clickLoginShouldFailWhenUserIsInvalid()
                .getLoginErrorMessage();

        assertEquals(loginErrorMessage, "Username and password do not match any user in this service");
    }

    @Test
    public void shouldNotLoginWithInvalidPassword() {
        driver.get(Config.BASE_URL);

        String loginErrorMessage = new LoginPage(driver)
                .enterUsername(Config.USERNAME)
                .enterPassword("invalid_password")
                .clickLoginShouldFailWhenUserIsInvalid()
                .getLoginErrorMessage();

        assertEquals(loginErrorMessage, "Username and password do not match any user in this service");
    }
}
