package pl.krywion.store_api;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class StoreApiApplication {
    private static Logger logger = LoggerFactory.getLogger(StoreApiApplication.class);

    public static void main(String[] args) {
        logger.info("Application started");
        SpringApplication.run(StoreApiApplication.class, args);

    }

}
