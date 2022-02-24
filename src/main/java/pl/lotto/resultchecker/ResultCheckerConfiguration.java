package pl.lotto.resultchecker;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import pl.lotto.lottonumbergenerator.LottoNumberGenerator;
import pl.lotto.numberreceiver.NumberReceiverFacade;

@Configuration
public class ResultCheckerConfiguration {

    @Bean
    WinnersRepository winnersRepository() {
        return new InMemoryWinnersRepository();
    }

    @Bean
    public ResultCheckerFacade resultCheckerFacade(WinnersRepository winnersRepository,
                                                   NumberReceiverFacade numberReceiverFacade,
                                                   LottoNumberGenerator lottoNumberGenerator) {
        return new ResultCheckerFacade(winnersRepository, numberReceiverFacade, lottoNumberGenerator);
    }

    public ResultCheckerFacade resultCheckerFacadeForTests(NumberReceiverFacade numberReceiverFacade,
                                                           LottoNumberGenerator lottoNumberGenerator){
        return resultCheckerFacade(new InMemoryWinnersRepository(), numberReceiverFacade, lottoNumberGenerator);
    }
}
