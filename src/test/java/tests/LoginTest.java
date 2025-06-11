package tests;

import base.BaseTest;
import org.junit.jupiter.api.Test;
import pages.LoginPage;

import static org.junit.jupiter.api.Assertions.assertTrue;

public class LoginTest extends BaseTest {

    @Test
    public void shouldLoginSuccessfully() {
        driver.get("https://www.saucedemo.com");

        boolean loggedIn = new LoginPage(driver)
                .enterUsername("standard_user")
                .enterPassword("secret_sauce")
                .clickLogin()
                .isHomePageLogo();

        assertTrue(loggedIn, "Deveria exibir mensagem de boas-vindas após login com sucesso");
    }
}
