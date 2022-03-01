package pl.lotto.resultchecker;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import pl.lotto.lottonumbergenerator.LottoNumberGeneratorFacade;
import pl.lotto.numberreceiver.NumberReceiverFacade;

@Configuration
public class ResultCheckerConfiguration {

    @Bean
    WinnersRepository winnersRepository() {
        return new InMemoryWinnersRepository();
    }

    @Bean
    ResultCheckerFacade resultCheckerFacade(WinnersRepository winnersRepository,
                                                   NumberReceiverFacade numberReceiverFacade,
                                                   LottoNumberGeneratorFacade lottoNumberGeneratorFacade) {
        return new ResultCheckerFacade(winnersRepository, numberReceiverFacade, lottoNumberGeneratorFacade);
    }

    public ResultCheckerFacade resultCheckerFacadeForTests(NumberReceiverFacade numberReceiverFacade,
                                                           LottoNumberGeneratorFacade lottoNumberGeneratorFacade){
        return resultCheckerFacade(new InMemoryWinnersRepository(), numberReceiverFacade, lottoNumberGeneratorFacade);
    }
}
