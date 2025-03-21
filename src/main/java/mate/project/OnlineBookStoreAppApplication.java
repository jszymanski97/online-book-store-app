package mate.project;

import io.github.cdimascio.dotenv.Dotenv;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class OnlineBookStoreAppApplication {
    private static final Logger logger = LoggerFactory
            .getLogger(OnlineBookStoreAppApplication.class);

    public static void main(String[] args) {
        Dotenv dotenv = Dotenv.load();

        dotenv.entries().forEach(entry -> System.setProperty(entry.getKey(), entry.getValue()));

        logEnv(dotenv,"MYSQLDB_DATABASE");
        logEnv(dotenv,"MYSQLDB_USER");
        logEnv(dotenv,"MYSQLDB_PASSWORD");
        logEnv(dotenv,"MYSQLDB_DOCKER_PORT");
        logEnv(dotenv,"JWT_EXPIRATION");
        logEnv(dotenv,"JWT_SECRET");

        SpringApplication.run(OnlineBookStoreAppApplication.class, args);
    }

    private static void logEnv(Dotenv dotenv, String var) {
        String value = dotenv.get(var);
        logger.info("{} = {}", var, value);
    }
}
