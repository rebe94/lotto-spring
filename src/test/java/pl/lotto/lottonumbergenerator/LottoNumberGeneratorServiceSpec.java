package pl.lotto.lottonumbergenerator;

import com.github.tomakehurst.wiremock.WireMockServer;
import com.github.tomakehurst.wiremock.client.WireMock;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.util.SocketUtils;
import org.springframework.web.client.RestTemplate;
import pl.lotto.lottonumbergenerator.dto.WinningNumbersDto;

import java.time.Duration;
import java.time.LocalDate;
import java.util.Set;

import static com.github.tomakehurst.wiremock.core.WireMockConfiguration.options;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.greaterThanOrEqualTo;
import static org.hamcrest.Matchers.lessThanOrEqualTo;
import static org.junit.jupiter.api.Assertions.assertAll;
import static org.mockito.BDDMockito.given;
import static pl.lotto.configuration.GameConfiguration.AMOUNT_OF_NUMBERS;
import static pl.lotto.configuration.GameConfiguration.HIGHEST_NUMBER;
import static pl.lotto.configuration.GameConfiguration.LOWEST_NUMBER;

@Tag("SpringTest")
@SpringBootTest
class LottoNumberGeneratorServiceSpec {

    private final WinningNumbersDto winningNumbersDto = new WinningNumbersDto(Set.of(1, 2, 3, 4, 5, 6));
    private final LocalDate SOME_DATE = LocalDate.of(2000, 1, 1);

    int port = SocketUtils.findAvailableTcpPort();
    String urlServiceForTests = "http://localhost:" + port + "/";
    WireMockServer wireMockServer;

    RestTemplate restTemplateForTests() {
        return new RestTemplateBuilder()
                .setConnectTimeout(Duration.ofMillis(1000))
                .setReadTimeout(Duration.ofMillis(1000))
                .build();
    }

    /*private final LottoNumberGenerator service = new LottoNumberGeneratorConfiguration()
            .lottoNumberGenerator(restTemplateForTests(), urlServiceForTests);*/

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
    @DisplayName("Should send request to lotto number generator service and receive winning numbers according to generate configuration")
    public void should_send_request_to_lotto_number_generator_service_and_receive_winning_numbers_according_to_generate_configuration() {
        LocalDate SOME_DATE = LocalDate.of(2000, 1, 1);
        String requestParameters = "year=" + SOME_DATE.getYear() + "&" +
                "month=" + SOME_DATE.getMonthValue() + "&" +
                "day=" + SOME_DATE.getDayOfMonth();
        String path = "/winningnumbers?" + requestParameters;

       /* WireMock.stubFor(WireMock.get(path)
                        .willReturn(WireMock.);*/
    }

}