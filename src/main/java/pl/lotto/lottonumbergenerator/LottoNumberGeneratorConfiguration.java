package pl.lotto.lottonumbergenerator;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.client.RestTemplate;
import pl.lotto.configuration.dto.GenerateConfigurationDto;

import static pl.lotto.configuration.GameConfiguration.AMOUNT_OF_NUMBERS;
import static pl.lotto.configuration.GameConfiguration.HIGHEST_NUMBER;
import static pl.lotto.configuration.GameConfiguration.LOWEST_NUMBER;

@Configuration
class LottoNumberGeneratorConfiguration {

    @Bean
    RestTemplate restTemplate() {
        return new RestTemplate();
    }

    @Bean
    LottoNumberGenerator lottoNumberGenerator(RestTemplate restTemplate) {
        return new LottoNumberGeneratorProxyImpl(restTemplate);
    }

    @Bean
    LottoNumberGeneratorFacade lottoNumberGeneratorFacade(LottoNumberGenerator lottoNumberGenerator) {
        return new LottoNumberGeneratorFacade(lottoNumberGenerator);
    }
}
