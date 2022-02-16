package pl.lotto.numberreceiver;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class NumberReceiverConfiguration {

    @Bean
    NumberRepository numberRepository() {
        return new InMemoryNumberRepository();
    }

    @Bean
    public NumberReceiverFacade numberReceiverFacade(NumberRepository numberRepository) {
        NumberValidator numberValidator = new NumberValidatorImpl();
        return new NumberReceiverFacade(numberValidator, numberRepository);
    }

    public NumberReceiverFacade numberReceiverFacadeForTests(){
        return numberReceiverFacade(new InMemoryNumberRepository());
    }
}
