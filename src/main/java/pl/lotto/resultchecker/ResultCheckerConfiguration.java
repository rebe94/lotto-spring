package pl.lotto.resultchecker;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import pl.lotto.lottonumbergenerator.LottoNumberGeneratorFacade;
import pl.lotto.numberreceiver.InMemoryNumberRepository;
import pl.lotto.numberreceiver.NumberReceiverFacade;
import pl.lotto.numberreceiver.NumberValidatorImpl;

@Configuration
public class ResultCheckerConfiguration {

    @Bean
    WinnersRepository winnersRepository() {
        return new InMemoryWinnersRepository();
    }

    @Bean
    public ResultCheckerFacade resultCheckerFacade(WinnersRepository winnersRepository,
                                                   NumberReceiverFacade numberReceiverFacade,
                                                   LottoNumberGeneratorFacade lottoNumberGeneratorFacade) {
        return new ResultCheckerFacade(winnersRepository, numberReceiverFacade, lottoNumberGeneratorFacade);
    }

    public ResultCheckerFacade resultCheckerFacadeForTests(NumberReceiverFacade numberReceiverFacade,
                                                           LottoNumberGeneratorFacade lottoNumberGeneratorFacade){
        return resultCheckerFacade(new InMemoryWinnersRepository(), numberReceiverFacade, lottoNumberGeneratorFacade);
    }
}
