package pl.lotto.resultchecker;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import pl.lotto.lottonumbergenerator.LottoNumberGeneratorFacade;
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
                                                   LottoNumberGeneratorFacade lottoNumberGeneratorFacade,
                                                   GenerateNumbersProxy generateNumbersProxy) {
        return new ResultCheckerFacade(winnersRepository, numberReceiverFacade, lottoNumberGeneratorFacade, generateNumbersProxy);
    }

    public ResultCheckerFacade resultCheckerFacadeForTests(NumberReceiverFacade numberReceiverFacade,
                                                           LottoNumberGeneratorFacade lottoNumberGeneratorFacade,
                                                           GenerateNumbersProxy generateNumbersProxy){
        return resultCheckerFacade(new InMemoryWinnersRepository(), numberReceiverFacade, lottoNumberGeneratorFacade, generateNumbersProxy);
    }
}
