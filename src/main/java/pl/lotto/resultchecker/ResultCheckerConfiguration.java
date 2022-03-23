package pl.lotto.resultchecker;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.EnableScheduling;
import pl.lotto.lottonumbergenerator.LottoNumberGeneratorFacade;
import pl.lotto.numberreceiver.NumberReceiverFacade;

@Configuration
@EnableScheduling
public class ResultCheckerConfiguration {

    @Bean
    ResultCheckerFacade resultCheckerFacade(WinnerRepository winnerRepository,
                                            NumberReceiverFacade numberReceiverFacade,
                                            LottoNumberGeneratorFacade lottoNumberGeneratorFacade) {
        return new ResultCheckerFacade(winnerRepository, numberReceiverFacade, lottoNumberGeneratorFacade);
    }

    @Bean
    WinnerDataLoader winnerDataLoader(WinnerRepository winnerRepository) {
        return new WinnerDataLoader(winnerRepository);
    }

    public ResultCheckerFacade resultCheckerFacadeForTests(NumberReceiverFacade numberReceiverFacade,
                                                           LottoNumberGeneratorFacade lottoNumberGeneratorFacade) {
        return resultCheckerFacade(new InMemoryWinnerRepository(), numberReceiverFacade, lottoNumberGeneratorFacade);
    }
}
