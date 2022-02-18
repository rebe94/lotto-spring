package pl.lotto.lottonumbergenerator;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class LottoNumberGeneratorConfiguration {

    @Bean
    public LottoNumberGeneratorFacade lottoNumberGeneratorFacade() {
        return new LottoNumberGeneratorFacade();
    }
}
