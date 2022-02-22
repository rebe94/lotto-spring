package pl.lotto.resultchecker;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import pl.lotto.numberreceiver.NumberReceiverFacade;
import pl.lotto.proxy.GenerateNumbersProxy;

@Configuration
public class ResultCheckerConfiguration {

    @Bean
    WinnersRepository winnersRepository() {
        return new InMemoryWinnersRepository();
    }

    @Bean
    public ResultCheckerFacade resultCheckerFacade(WinnersRepository winnersRepository,
                                                   NumberReceiverFacade numberReceiverFacade,
                                                   GenerateNumbersProxy generateNumbersProxy) {
        return new ResultCheckerFacade(winnersRepository, numberReceiverFacade, generateNumbersProxy);
    }

    public ResultCheckerFacade resultCheckerFacadeForTests(NumberReceiverFacade numberReceiverFacade,
                                                           GenerateNumbersProxy generateNumbersProxy){
        return resultCheckerFacade(new InMemoryWinnersRepository(), numberReceiverFacade, generateNumbersProxy);
    }
}
