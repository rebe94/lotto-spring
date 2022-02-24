package pl.lotto.lottonumbergenerator;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;
import pl.lotto.lottonumbergenerator.dto.GenerateConfiguration;
import pl.lotto.lottonumbergenerator.dto.WinningNumbers;

import java.util.UUID;

public class LottoNumberGeneratorProxy implements LottoNumberGenerator {

    private final RestTemplate rest;
    private final GenerateConfiguration generateConfiguration;

    @Value("${name.service.url}")
    private String generateServiceUrl;

    public LottoNumberGeneratorProxy(RestTemplate rest, GenerateConfiguration generateConfiguration) {
        this.rest = rest;
        this.generateConfiguration = generateConfiguration;
    }

    public WinningNumbers generateNumbers() {
        String requestParameters = "amountOfNumbers=" + generateConfiguration.getAmountOfNumbers() + "&" +
                "lowestNumber=" + generateConfiguration.getLowestNumber() + "&" +
                "highestNumber=" + generateConfiguration.getHighestNumber();

        String uri = generateServiceUrl + "/generator?" + requestParameters;

        HttpHeaders headers = new HttpHeaders();
        headers.add("requestId", UUID.randomUUID().toString());

        HttpEntity<HttpHeaders> httpEntity = new HttpEntity<>(headers);

        ResponseEntity<WinningNumbers> response =
                rest.exchange(uri,
                        HttpMethod.GET,
                        httpEntity,
                        WinningNumbers.class);

        return response.getBody();
    }
}

