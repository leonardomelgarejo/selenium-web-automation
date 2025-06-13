package base.factory;

import config.Config;
import org.openqa.selenium.chrome.ChromeOptions;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

public class ChromeOptionsFactory {

    public static ChromeOptions build() throws IOException {
        ChromeOptions options = new ChromeOptions();

        if (Config.HEADLESS) {
            options.addArguments("--headless=new");
        }

        for (String arg : Config.CHROME_ARGS) {
            options.addArguments(arg);
        }

        Path profileDir = Files.createTempDirectory("chrome-profile");
        options.addArguments("--user-data-dir=" + profileDir.toString());

        return options;
    }
}