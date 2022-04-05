package pl.lotto;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.mongodb.repository.config.EnableMongoRepositories;
import org.springframework.scheduling.annotation.EnableScheduling;
import pl.lotto.configuration.TimeConfiguration;

import javax.annotation.PostConstruct;
import java.util.TimeZone;

@SpringBootApplication
@EnableMongoRepositories
@EnableScheduling
public class AppRunner {

    public static void main(String[] args) {
        SpringApplication.run(AppRunner.class, args);
    }

    @PostConstruct
    public void init(){
        TimeZone timeZone = TimeZone.getTimeZone("Europe/Warsaw");
        TimeConfiguration.setZoneClock(timeZone.toZoneId());
    }
}
