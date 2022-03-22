package pl.lotto;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.data.mongodb.repository.config.EnableMongoRepositories;
import pl.lotto.numberreceiver.NumberReceiverFacade;

import java.util.Set;

@SpringBootApplication
@EnableMongoRepositories
public class AppRunner {

    public static void main(String[] args) {
        ConfigurableApplicationContext con = SpringApplication.run(AppRunner.class, args);

        NumberReceiverFacade receiver = con.getBean(NumberReceiverFacade.class);
       /* System.out.println(receiver.inputNumbers(Set.of(1,2,3,4,5,6)));
        System.out.println(receiver.inputNumbers(Set.of(1,2,3,4,5,7)));
        System.out.println(receiver.inputNumbers(Set.of(1,2,3,4,5,8)));*/
    }

}
