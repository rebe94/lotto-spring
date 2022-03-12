package pl.lotto;

import com.github.tomakehurst.wiremock.WireMockServer;
import com.github.tomakehurst.wiremock.client.WireMock;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.util.SocketUtils;
import org.springframework.web.client.RestTemplate;
import pl.lotto.lottonumbergenerator.LottoNumberGenerator;
import pl.lotto.lottonumbergenerator.LottoNumberGeneratorConfiguration;
import pl.lotto.lottonumbergenerator.LottoNumberGeneratorFacade;
import pl.lotto.lottonumbergenerator.dto.WinningNumbersDto;
import pl.lotto.numberreceiver.NumberReceiverConfiguration;
import pl.lotto.numberreceiver.NumberReceiverFacade;
import pl.lotto.numberreceiver.ResultMessage;
import pl.lotto.resultannouncer.ResultAnnouncerFacade;
import pl.lotto.resultchecker.ResultCheckerConfiguration;
import pl.lotto.resultchecker.ResultCheckerFacade;

import java.time.Duration;
import java.time.LocalDate;
import java.util.Set;

import static com.github.tomakehurst.wiremock.client.WireMock.urlPathEqualTo;
import static com.github.tomakehurst.wiremock.client.WireMock.urlPathMatching;
import static com.github.tomakehurst.wiremock.core.WireMockConfiguration.options;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;

public class UserWinsIntegrationSpec extends BaseIntegrationSpec {

    //final LocalDate SOME_DATE = LocalDate.of(2000, 1, 1);

    private static final int port = SocketUtils.findAvailableTcpPort();
    private static final String urlServiceForTests = "http://localhost:" + port + "/";
    private WireMockServer wireMockServer;

    @TestConfiguration
    public static class IntegrationTestConfiguration {

        @Bean
        RestTemplate restTemplate() {
            return new RestTemplateBuilder()
                    .setConnectTimeout(Duration.ofMillis(1000))
                    .setReadTimeout(Duration.ofMillis(1000))
                    .build();
        }

        @Bean
        String generateServiceUrl() {
            return urlServiceForTests;
        }
    }

    @Autowired
    private NumberReceiverFacade numberReceiverFacade;
    @Autowired
    private ResultCheckerFacade resultCheckerFacade;
    @Autowired
    private ResultAnnouncerFacade resultAnnouncerFacade;

    @BeforeEach
    void setup() {
        wireMockServer = new WireMockServer(options().port(port));
        wireMockServer.start();
        WireMock.configureFor(port);
        System.out.println("test: " + urlServiceForTests);
    }

    @AfterEach
    void tearDown() {
        wireMockServer.stop();
    }

    @Test
    public void user_chooses_correct_numbers_and_receives_acceptance_and_hash_code_then_checks_result_and_receives_win_information() {
        // given
        // when
        final ResultMessage receivedUserMessage = userChoosesNumbers(Set.of(1, 2, 3, 4, 5, 6));
        final String GENERATED_HASH = receivedUserMessage.getHash();
        final String DRAW_DATE = receivedUserMessage.getDrawingDate();
        final ResultMessage numbersAccepted = new ResultMessage("Accepted", GENERATED_HASH, DRAW_DATE);
        // then
        assertThat(receivedUserMessage, equalTo(numbersAccepted));

        // given
        generatorDrawsWinningNumbers("1, 2, 3, 4, 5, 6");

        // when
        ticketsAreCheckingAndMarkingIfTheyWin();

        final String checkResultMessage = userCheckResultByHash(GENERATED_HASH);
        // then
        assertThat(checkResultMessage, equalTo("Winner"));
    }

    private ResultMessage userChoosesNumbers(Set<Integer> numbers) {
        return numberReceiverFacade.inputNumbers(numbers);
    }

    private void generatorDrawsWinningNumbers(String generatedNumbers) {
        WireMock.stubFor(WireMock.get(urlPathEqualTo("/winningnumbers"))
                .willReturn(WireMock.aResponse()
                        .withBody("{" +
                                "\"winningNumbers\" : [" + generatedNumbers + "]" +
                                "}")
                        .withHeader("Content-Type", "application/json")));
    }

    /*private String createRequestParametersOfDate(LocalDate date) {
        String day = date.getDayOfMonth() < 10 ? "0" + date.getDayOfMonth() : "" + date.getDayOfMonth();
        String month = date.getMonthValue() < 10 ? "0" + date.getMonthValue() : "" + date.getMonthValue();
        String year = String.valueOf(date.getYear());
        return "year=" + year + "&" +
                "month=" + month + "&" +
                "day=" + day;
    }*/

    private void ticketsAreCheckingAndMarkingIfTheyWin() {
        resultCheckerFacade.checkWinnersAfterDraw();
    }

    private String userCheckResultByHash(String generatedHash) {
        return resultAnnouncerFacade.checkResult(generatedHash);
    }
}
