package pl.lotto.numberreceiver;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class NumberReceiverConfiguration {

    @Bean
    NumberReceiverFacade numberReceiverFacade(TicketRepository ticketRepository) {
        NumberValidator numberValidator = new NumberValidatorImpl();
        return new NumberReceiverFacade(numberValidator, ticketRepository);
    }

    public NumberReceiverFacade numberReceiverFacadeForTests(){
        return numberReceiverFacade(new InMemoryTicketRepository());
    }
}
