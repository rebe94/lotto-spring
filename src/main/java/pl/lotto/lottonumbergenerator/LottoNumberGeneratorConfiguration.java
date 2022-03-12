package pl.lotto.lottonumbergenerator;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.context.annotation.Profile;
import org.springframework.web.client.RestTemplate;

import java.time.Duration;

@Configuration
public class LottoNumberGeneratorConfiguration {

    @Value("${name.generator.service.url}")
    private String generateServiceUrl;

   /* @Bean
    RestTemplate restTemplate() {
        return new RestTemplate();
    }*/

    /*@Bean
    String generateServiceUrl() {
        return generateServiceUrl;
    }*/

    @Bean
    LottoNumberGenerator lottoNumberGenerator(RestTemplate restTemplate) {
        return new LottoNumberGeneratorServiceImpl(restTemplate, generateServiceUrl);
    }

    @Bean
    LottoNumberGeneratorFacade lottoNumberGeneratorFacade(LottoNumberGenerator lottoNumberGenerator) {
        return new LottoNumberGeneratorFacade(lottoNumberGenerator);
    }


}
