package pl.lotto.lottonumbergenerator;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.client.RestTemplate;
import pl.lotto.lottonumbergenerator.dto.GenerateConfiguration;

import static pl.lotto.configuration.GameConfiguration.AMOUNT_OF_NUMBERS;
import static pl.lotto.configuration.GameConfiguration.HIGHEST_NUMBER;
import static pl.lotto.configuration.GameConfiguration.LOWEST_NUMBER;

@Configuration
public class LottoNumberGeneratorProxyConfiguration {

    @Bean
    RestTemplate restTemplate() {
        return new RestTemplate();
    }

    @Bean
    GenerateConfiguration generateConfiguration() {
        return new GenerateConfiguration(AMOUNT_OF_NUMBERS, LOWEST_NUMBER, HIGHEST_NUMBER);
    }

    @Bean
    public LottoNumberGeneratorProxy lottoNumberGeneratorProxy(RestTemplate restTemplate, GenerateConfiguration generateConfiguration) {
        return new LottoNumberGeneratorProxy(restTemplate, generateConfiguration);
    }
}
