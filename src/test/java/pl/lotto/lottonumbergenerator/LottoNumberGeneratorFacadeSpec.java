package pl.lotto.lottonumbergenerator;

import com.github.tomakehurst.wiremock.WireMockServer;
import com.github.tomakehurst.wiremock.client.WireMock;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;
import org.springframework.util.SocketUtils;
import pl.lotto.lottonumbergenerator.dto.WinningNumbersDto;

import static com.github.tomakehurst.wiremock.client.WireMock.urlPathEqualTo;
import static com.github.tomakehurst.wiremock.core.WireMockConfiguration.options;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.MatcherAssert.assertThat;

import java.time.LocalDate;

@Tag("WithoutSpringTest")
class LottoNumberGeneratorFacadeSpec {

    private final int port = SocketUtils.findAvailableTcpPort();
    private final String urlServiceForTest = "http://localhost:" + port + "/";
    private final LocalDate DRAW_DATE = LocalDate.of(2000, 1, 1);

    private final LottoNumberGeneratorFacade lottoNumberGeneratorFacade = new LottoNumberGeneratorConfiguration()
            .lottoNumberGeneratorFacadeForTests(urlServiceForTest);
    private WireMockServer wireMockServer;

    @BeforeEach
    void setup() {
        wireMockServer = new WireMockServer(options().port(port));
        wireMockServer.start();
        WireMock.configureFor(port);
    }

    @AfterEach
    void tearDown() {
        wireMockServer.stop();
    }

    @Test
    public void generator_service_responds_with_correct_numbers_and_then_returns_valid_message() {
        // given
        generatorDrawsWinningNumbersForDrawDay("1, 2, 3, 4, 5, 6");
        // when
        WinningNumbersDto winningNumbersDto = lottoNumberGeneratorFacade.getWinningNumbers(DRAW_DATE);
        WinningNumbersDto.ValidationMessage validationMessage = winningNumbersDto.getValidationMessage();

        // then
        assertThat(validationMessage, equalTo(WinningNumbersDto.ValidationMessage.VALID));
    }

    @Test
    public void generator_service_responds_with_too_few_numbers_and_then_returns_not_valid_message() {
        // given
        generatorDrawsWinningNumbersForDrawDay("1, 2, 3, 4, 5");
        // when
        WinningNumbersDto winningNumbersDto = lottoNumberGeneratorFacade.getWinningNumbers(DRAW_DATE);
        WinningNumbersDto.ValidationMessage validationMessage = winningNumbersDto.getValidationMessage();

        // then
        assertThat(validationMessage, equalTo(WinningNumbersDto.ValidationMessage.NOT_VALID));
    }

    @Test
    public void generator_service_responds_with_out_of_range_numbers_and_then_returns_not_valid_message() {
        // given
        generatorDrawsWinningNumbersForDrawDay("1, 2, 3, 4, 5, 100");
        // when
        WinningNumbersDto winningNumbersDto = lottoNumberGeneratorFacade.getWinningNumbers(DRAW_DATE);
        WinningNumbersDto.ValidationMessage validationMessage = winningNumbersDto.getValidationMessage();

        // then
        assertThat(validationMessage, equalTo(WinningNumbersDto.ValidationMessage.NOT_VALID));
    }

    @Test
    public void generator_service_responds_with_incorrect_data_and_then_returns_failed_message() {
        // given
        generatorDrawsWinningNumbersForDrawDay("Not numbers");
        // when
        WinningNumbersDto winningNumbersDto = lottoNumberGeneratorFacade.getWinningNumbers(DRAW_DATE);
        WinningNumbersDto.ValidationMessage validationMessage = winningNumbersDto.getValidationMessage();

        // then
        assertThat(validationMessage, equalTo(WinningNumbersDto.ValidationMessage.FAILED));
    }

    private void generatorDrawsWinningNumbersForDrawDay(String generatedNumbers) {
        WireMock.stubFor(WireMock.get(urlPathEqualTo("/winningnumbers"))
                .willReturn(WireMock.aResponse()
                        .withBody("{\"winningNumbers\" : [" + generatedNumbers + "]}")
                        .withHeader("Content-Type", "application/json")));
    }

    @Test
    public void generator_service_responds_with_null_value_and_then_returns_failed_message() {
        // given
        WireMock.stubFor(WireMock.get(urlPathEqualTo("/winningnumbers"))
                .willReturn(WireMock.aResponse()
                        .withBody("{\"winningNumbers\" : null }")
                        .withHeader("Content-Type", "application/json")));
        // when
        WinningNumbersDto winningNumbersDto = lottoNumberGeneratorFacade.getWinningNumbers(DRAW_DATE);
        WinningNumbersDto.ValidationMessage validationMessage = winningNumbersDto.getValidationMessage();

        // then
        assertThat(validationMessage, equalTo(WinningNumbersDto.ValidationMessage.FAILED));
    }

    @Test
    public void generator_service_responds_with_empty_data_and_then_returns_failed_message() {
        // given
        WireMock.stubFor(WireMock.get(urlPathEqualTo("/winningnumbers"))
                .willReturn(WireMock.aResponse()
                        .withBody("")
                        .withHeader("Content-Type", "application/json")));
        // when
        WinningNumbersDto winningNumbersDto = lottoNumberGeneratorFacade.getWinningNumbers(DRAW_DATE);
        WinningNumbersDto.ValidationMessage validationMessage = winningNumbersDto.getValidationMessage();

        // then
        assertThat(validationMessage, equalTo(WinningNumbersDto.ValidationMessage.FAILED));
    }

    @Test
    public void generator_service_responds_with_empty_set_of_numbers_and_then_returns_not_exist_message() {
        // given
        WireMock.stubFor(WireMock.get(urlPathEqualTo("/winningnumbers"))
                .willReturn(WireMock.aResponse()
                        .withBody("{\"winningNumbers\":[]}")
                        .withStatus(HttpStatus.NOT_FOUND.value())
                        .withHeader("Content-Type", "application/json")));
        // when
        WinningNumbersDto winningNumbersDto = lottoNumberGeneratorFacade.getWinningNumbers(DRAW_DATE);
        WinningNumbersDto.ValidationMessage validationMessage = winningNumbersDto.getValidationMessage();

        // then
        assertThat(validationMessage, equalTo(WinningNumbersDto.ValidationMessage.NOT_EXIST));
    }





}