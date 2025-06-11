package tests;

import base.BaseTest;
import io.qameta.allure.Feature;
import org.junit.jupiter.api.Test;
import pages.LoginPage;

import static org.junit.jupiter.api.Assertions.assertTrue;

@Feature("Login")
public class LoginTest extends BaseTest {

    @Test
    public void shouldLoginSuccessfully() {
        driver.get("https://www.saucedemo.com");

        boolean loggedIn = new LoginPage(driver)
                .enterUsername("standard_user")
                .enterPassword("secret_sauce")
                .clickLogin()
                .isHomePageLogo();

        assertTrue(loggedIn, "Deveria exibir a homepage");
    }
}
