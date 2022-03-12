package pl.lotto.resultannouncer;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import pl.lotto.resultchecker.ResultCheckerFacade;

@Configuration
public class ResultAnnouncerConfiguration {

    @Bean
    public pl.lotto.resultannouncer.ResultAnnouncerFacade resultAnnouncerFacade(ResultCheckerFacade resultCheckerFacade) {
        return new pl.lotto.resultannouncer.ResultAnnouncerFacade(resultCheckerFacade);
    }
}
