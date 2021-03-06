package pl.lotto.lottonumbergenerator;

import lombok.Getter;
import org.slf4j.Logger;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;
import pl.lotto.lottonumbergenerator.dto.WinningNumbersDto;

import java.time.LocalDate;
import java.util.Set;

import static org.slf4j.LoggerFactory.getLogger;
import static pl.lotto.lottonumbergenerator.LottoNumberGeneratorMessageProvider.failed;
import static pl.lotto.lottonumbergenerator.LottoNumberGeneratorMessageProvider.not_valid;
import static pl.lotto.lottonumbergenerator.LottoNumberGeneratorMessageProvider.numbers_not_exist;
import static pl.lotto.lottonumbergenerator.LottoNumberGeneratorMessageProvider.valid;

class LottoNumberGeneratorClientImpl implements LottoNumberGenerator {

    private static final Logger LOGGER = getLogger(LottoNumberGeneratorClientImpl.class.getName());

    private final RestTemplate rest;
    private final String generateServiceUrl;
    private final WinningNumberValidator winningNumberValidator;

    LottoNumberGeneratorClientImpl(RestTemplate rest, String generateServiceUrl, WinningNumberValidator winningNumberValidator) {
        this.rest = rest;
        this.generateServiceUrl = generateServiceUrl;
        this.winningNumberValidator = winningNumberValidator;
    }

    @Getter
    private static class ReceivedNumbersDto {
        Set<Integer> winningNumbers;
    }

    public WinningNumbersDto getWinningNumbersRequest(LocalDate date) {
        String requestParameters = createRequestParametersOfDate(date);
        String uri = generateServiceUrl + "/winningnumbers?" + requestParameters;
        HttpEntity<HttpHeaders> httpEntity = new HttpEntity<>(new HttpHeaders());

        try {
            ResponseEntity<ReceivedNumbersDto> response = rest.exchange(
                    uri,
                    HttpMethod.GET,
                    httpEntity,
                    ReceivedNumbersDto.class);
            return verifyResponse(response);
        } catch (HttpClientErrorException.NotFound notFoundException) {
            return numbers_not_exist();
        } catch (RuntimeException exception) {
            processException(exception);
        }
        return failed();
    }

    private String createRequestParametersOfDate(LocalDate date) {
        String day = String.valueOf(date.getDayOfMonth());
        String month = String.valueOf(date.getMonthValue());
        String year = String.valueOf(date.getYear());
        return "year=" + year + "&" +
                "month=" + month + "&" +
                "day=" + day;
    }

    private WinningNumbersDto verifyResponse(ResponseEntity<ReceivedNumbersDto> response) {
        if (winningNumberValidator.isNumberValid(response.getBody().getWinningNumbers())) {
            return valid(response.getBody().getWinningNumbers());
        }
        return not_valid(response.getBody().getWinningNumbers());
    }

    private void processException(Exception exception) {
        LOGGER.error("Http request execution failed.", exception);
    }
}

