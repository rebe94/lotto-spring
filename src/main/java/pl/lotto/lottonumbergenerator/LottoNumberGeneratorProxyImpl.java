package pl.lotto.lottonumbergenerator;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;
import pl.lotto.lottonumbergenerator.dto.WinningNumbersDto;

import java.time.LocalDate;
import java.util.UUID;

public class LottoNumberGeneratorProxyImpl implements LottoNumberGenerator {

    private final RestTemplate rest;

    @Value("${name.generator.service.url}")
    private String generateServiceUrl;

    LottoNumberGeneratorProxyImpl(RestTemplate rest) {
        this.rest = rest;
    }

    public WinningNumbersDto getWinningNumbers(LocalDate date) {
        String requestParameters = "year=" + date.getYear() + "&" +
                "month=" + date.getMonthValue() + "&" +
                "day=" + date.getDayOfMonth();

        String uri = generateServiceUrl + "/winningnumbers?" + requestParameters;

        HttpHeaders headers = new HttpHeaders();

        HttpEntity<HttpHeaders> httpEntity = new HttpEntity<>(headers);

        ResponseEntity<WinningNumbersDto> response =
                rest.exchange(uri,
                        HttpMethod.GET,
                        httpEntity,
                        WinningNumbersDto.class);

        return response.getBody();
    }
}

