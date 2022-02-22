package pl.lotto.proxy;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;
import pl.lotto.model.GenerateConfiguration;

import java.util.Arrays;
import java.util.Set;
import java.util.TreeSet;
import java.util.UUID;
import java.util.stream.Collectors;

import static pl.lotto.configuration.GameConfiguration.AMOUNT_OF_NUMBERS;
import static pl.lotto.configuration.GameConfiguration.HIGHEST_NUMBER;
import static pl.lotto.configuration.GameConfiguration.LOWEST_NUMBER;

@Component
public class GenerateNumbersProxy {

    private final RestTemplate rest;

    @Value("${name.service.url}")
    private String generateServiceUrl;

    public GenerateNumbersProxy(RestTemplate rest) {
        this.rest = rest;
    }

    public Set<Integer> generateNumbers() {
        String uri = generateServiceUrl + "/generate";

        HttpHeaders headers = new HttpHeaders();
        headers.add("requestId", UUID.randomUUID().toString());

        GenerateConfiguration generateConfiguration = GenerateConfiguration.of
                (6,1,99);
        //(AMOUNT_OF_NUMBERS, LOWEST_NUMBER, HIGHEST_NUMBER);
        HttpEntity<GenerateConfiguration> httpEntity = new HttpEntity<>(generateConfiguration, headers);

        ResponseEntity<Integer[]> response =
                rest.exchange(uri,
                        HttpMethod.GET,
                        httpEntity,
                        Integer[].class);

        Integer[] body = response.getBody();
        Set<Integer> set = Arrays.stream(body).collect(Collectors.toSet());
        return new TreeSet<>(set);
    }
}

