package pl.lotto;

import com.github.tomakehurst.wiremock.WireMockServer;
import com.github.tomakehurst.wiremock.client.WireMock;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.test.context.TestPropertySource;
import org.springframework.util.SocketUtils;
import org.springframework.web.client.RestTemplate;
import pl.lotto.lottonumbergenerator.LottoNumberGenerator;
import pl.lotto.lottonumbergenerator.LottoNumberGeneratorServiceImpl;
import pl.lotto.numberreceiver.NumberReceiverFacade;
import pl.lotto.numberreceiver.ResultMessage;
import pl.lotto.resultannouncer.ResultAnnouncerFacade;
import pl.lotto.resultchecker.ResultCheckerFacade;

import java.time.Duration;
import java.util.Set;

import static com.github.tomakehurst.wiremock.client.WireMock.urlPathEqualTo;
import static com.github.tomakehurst.wiremock.core.WireMockConfiguration.options;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;

@SpringBootTest
@TestPropertySource(locations="classpath:test.properties")
public class UserLosesIntegrationSpecTemp {

    @TestConfiguration
    public static class IntegrationTestConfiguration {

        RestTemplate restTemplateForTest() {
            return new RestTemplateBuilder()
                    .setConnectTimeout(Duration.ofMillis(1000))
                    .setReadTimeout(Duration.ofMillis(1000))
                    .build();
        }

        @Bean
        LottoNumberGenerator lottoNumberGenerator() {
            return new LottoNumberGeneratorServiceImpl(restTemplateForTest(), urlServiceForTest);
        }
    }

    private static final int port = SocketUtils.findAvailableTcpPort();
    private static final String urlServiceForTest = "http://localhost:" + port + "/";

    private WireMockServer wireMockServer;
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
        final ResultMessage numbersAcceptedMessage = new ResultMessage("Accepted", GENERATED_HASH, DRAW_DATE);
        // then
        assertThat(receivedUserMessage, equalTo(numbersAcceptedMessage));

        // given
        generatorDrawsWinningNumbers("1, 2, 3, 4, 5, 15");
        ticketsAreCheckingAndMarkingIfTheyWin();
        // when
        final String checkResultMessage = userCheckResultByHash(GENERATED_HASH);
        // then
        assertThat(checkResultMessage, equalTo("Loser"));
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

    private void ticketsAreCheckingAndMarkingIfTheyWin() {
        resultCheckerFacade.checkWinnersAfterDraw();
    }

    private String userCheckResultByHash(String generatedHash) {
        return resultAnnouncerFacade.checkResult(generatedHash);
    }
}
