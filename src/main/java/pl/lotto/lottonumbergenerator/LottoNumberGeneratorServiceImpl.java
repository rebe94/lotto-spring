package pl.lotto.lottonumbergenerator;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;
import pl.lotto.lottonumbergenerator.dto.WinningNumbersDto;

import java.time.LocalDate;

import static org.slf4j.LoggerFactory.getLogger;

public class LottoNumberGeneratorServiceImpl implements LottoNumberGenerator {

    private static final Logger LOGGER = getLogger(LottoNumberGeneratorServiceImpl.class.getName());

    private final RestTemplate rest;
    private final String generateServiceUrl;

    public LottoNumberGeneratorServiceImpl(RestTemplate rest, String generateServiceUrl) {
        this.rest = rest;
        this.generateServiceUrl = generateServiceUrl;
    }

    public WinningNumbersDto getWinningNumbers(LocalDate date) {
        String requestParameters = createRequestParametersOfDate(date);
        String uri = generateServiceUrl + "/winningnumbers?" + requestParameters;
        HttpEntity<HttpHeaders> httpEntity = new HttpEntity<>(new HttpHeaders());

        ResponseEntity<WinningNumbersDto> response = null;
        try {
            response = rest.exchange(uri,
                    HttpMethod.GET,
                    httpEntity,
                    WinningNumbersDto.class);
        } catch (RestClientException exception) {
            processException(exception);
        }
        return response.getBody();
    }

    private String createRequestParametersOfDate(LocalDate date) {
        String day = date.getDayOfMonth() < 10 ? "0" + date.getDayOfMonth() : "" + date.getDayOfMonth();
        String month = date.getMonthValue() < 10 ? "0" + date.getMonthValue() : "" + date.getMonthValue();
        String year = String.valueOf(date.getYear());
        return "year=" + year + "&" +
                "month=" + month + "&" +
                "day=" + day;
    }

    private void processException(RestClientException exception) {
        LOGGER.error("Http request execution failed.", exception);
    }
}

