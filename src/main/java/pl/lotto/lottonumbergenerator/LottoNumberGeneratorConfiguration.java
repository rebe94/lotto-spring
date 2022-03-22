package pl.lotto.lottonumbergenerator;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.client.RestTemplate;

import java.time.Duration;

@Configuration
public class LottoNumberGeneratorConfiguration {

    @Value("${name.generator.service.url}")
    String generateServiceUrl;

    private final RestTemplate restTemplate = new RestTemplateBuilder()
            .setConnectTimeout(Duration.ofMillis(1000))
            .setReadTimeout(Duration.ofMillis(1000))
            .build();

    @Bean
    LottoNumberGenerator lottoNumberGenerator() {
        WinningNumberValidator winningNumberValidator = new WinningNumberValidatorImpl();
        return new LottoNumberGeneratorClientImpl(restTemplate, generateServiceUrl, winningNumberValidator);
    }

    @Bean
    LottoNumberGeneratorFacade lottoNumberGeneratorFacade(LottoNumberGenerator lottoNumberGenerator) {
        return new LottoNumberGeneratorFacade(lottoNumberGenerator);
    }

    public LottoNumberGeneratorFacade lottoNumberGeneratorFacadeForTests(String generateServiceUrl) {
        LottoNumberGeneratorClientImpl lottoNumberGenerator = new LottoNumberGeneratorClientImpl(restTemplate,
                generateServiceUrl, new WinningNumberValidatorImpl());
        return lottoNumberGeneratorFacade(lottoNumberGenerator);
    }
}
