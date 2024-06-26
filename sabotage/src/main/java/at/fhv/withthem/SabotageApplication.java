package at.fhv.withthem;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;

@SpringBootApplication
@Configuration
@ComponentScan({"at.fhv.withthem.sabotages", "at.fhv.withthem.WebSocket"})
public class SabotageApplication {

    public static void main(String[] args) {
        SpringApplication.run(SabotageApplication.class, args);
    }
}