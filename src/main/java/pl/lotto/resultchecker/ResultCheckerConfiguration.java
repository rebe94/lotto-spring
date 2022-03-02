package pl.lotto.resultchecker;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import pl.lotto.lottonumbergenerator.LottoNumberGeneratorFacade;
import pl.lotto.numberreceiver.NumberReceiverFacade;

@Configuration
public class ResultCheckerConfiguration {

    @Bean
    WinnerRepository winnersRepository() {
        return new InMemoryWinnerRepository();
    }

    @Bean
    ResultCheckerFacade resultCheckerFacade(WinnerRepository winnerRepository,
                                            NumberReceiverFacade numberReceiverFacade,
                                            LottoNumberGeneratorFacade lottoNumberGeneratorFacade) {
        return new ResultCheckerFacade(winnerRepository, numberReceiverFacade, lottoNumberGeneratorFacade);
    }

    public ResultCheckerFacade resultCheckerFacadeForTests(NumberReceiverFacade numberReceiverFacade,
                                                           LottoNumberGeneratorFacade lottoNumberGeneratorFacade){
        return resultCheckerFacade(new InMemoryWinnerRepository(), numberReceiverFacade, lottoNumberGeneratorFacade);
    }
}
