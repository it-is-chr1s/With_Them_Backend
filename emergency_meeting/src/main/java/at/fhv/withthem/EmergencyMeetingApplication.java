package at.fhv.withthem;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;

@SpringBootApplication
@Configuration
@ComponentScan({"at.fhv.withthem.MeetingLogic", "at.fhv.withthem.WebSocket"})
public class EmergencyMeetingApplication {
    public static void main(String[] args) {
        SpringApplication.run(EmergencyMeetingApplication.class, args);
    }
}
