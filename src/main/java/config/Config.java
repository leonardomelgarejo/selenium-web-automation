package config;

import java.util.List;

public class Config {
    public static final String BASE_URL  = System.getenv("BASE_URL");
    public static final String USERNAME  = System.getenv("USERNAME");
    public static final String PASSWORD  = System.getenv("PASSWORD");

    public static final boolean HEADLESS =
            Boolean.parseBoolean(System.getenv().getOrDefault("HEADLESS", "true"));
    public static final List<String> CHROME_ARGS = List.of(
            System.getenv().getOrDefault("CHROME_ARG_1", "--no-sandbox"),
            System.getenv().getOrDefault("CHROME_ARG_2", "--disable-dev-shm-usage"),
            System.getenv().getOrDefault("CHROME_ARG_3", "--remote-allow-origins=*")
    );
    public static final int IMPLICIT_WAIT =
            Integer.parseInt(System.getenv().getOrDefault("IMPLICIT_WAIT", "5"));

    static {
        if (BASE_URL == null || USERNAME == null || PASSWORD == null) {
            throw new IllegalStateException(
                    "É preciso definir as variáveis de ambiente BASE_URL, USERNAME e PASSWORD"
            );
        }
    }
}
