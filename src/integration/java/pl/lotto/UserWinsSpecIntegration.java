package pl.lotto;

import com.github.tomakehurst.wiremock.WireMockServer;
import com.github.tomakehurst.wiremock.client.WireMock;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import org.mockito.MockedStatic;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.util.SocketUtils;
import pl.lotto.configuration.GameConfiguration;
import pl.lotto.lottonumbergenerator.LottoNumberGeneratorConfiguration;
import pl.lotto.lottonumbergenerator.LottoNumberGeneratorFacade;
import pl.lotto.numberreceiver.NumberReceiverFacade;
import pl.lotto.numberreceiver.Ticket;
import pl.lotto.numberreceiver.TicketRepository;
import pl.lotto.numberreceiver.dto.ResultMessageDto;
import pl.lotto.resultannouncer.ResultAnnouncerFacade;
import pl.lotto.resultchecker.ResultCheckerFacade;
import pl.lotto.resultchecker.WinnerRepository;

import java.time.LocalDate;
import java.util.Set;

import static com.github.tomakehurst.wiremock.client.WireMock.urlPathEqualTo;
import static com.github.tomakehurst.wiremock.core.WireMockConfiguration.options;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;
import static pl.lotto.numberreceiver.NumberReceiverMessageProvider.accepted;
import static pl.lotto.resultannouncer.ResultAnnouncerMessageProvider.win_message;

@Tag("SpringTest")
@SpringBootTest
class UserWinsSpecIntegration extends BaseSpecIntegration {

    private WireMockServer wireMockServer;
    @Autowired
    private NumberReceiverFacade numberReceiverFacade;
    @Autowired
    private ResultCheckerFacade resultCheckerFacade;
    @Autowired
    private ResultAnnouncerFacade resultAnnouncerFacade;
    @Autowired
    private TicketRepository ticketRepository;
    @Autowired
    private WinnerRepository winnerRepository;

    private static final int port = SocketUtils.findAvailableTcpPort();
    private static final String urlServiceForTest = "http://localhost:" + port + "/";

    private final LocalDate NEXT_DRAW_DATE = LocalDate.of(2000, 1, 1);
    private ResultMessageDto receivedUserMessage;
    private ResultMessageDto numbersAcceptedMessage;
    private String checkResultMessage;

    @TestConfiguration
    public static class IntegrationTestConfiguration {

        @Bean
        LottoNumberGeneratorFacade lottoNumberGeneratorFacade() {
            return new LottoNumberGeneratorConfiguration()
                    .lottoNumberGeneratorFacadeForTests(urlServiceForTest);
        }
    }

    @BeforeEach
    void setup() {
        wireMockServer = new WireMockServer(options().port(port));
        wireMockServer.start();
        WireMock.configureFor(port);
    }

    @AfterEach
    void tearDown() {
        wireMockServer.stop();
        ticketRepository.deleteAll();
        winnerRepository.deleteAll();
    }

    @Test
    public void user_chooses_correct_numbers_and_receives_acceptance_and_hash_code_then_checks_result_and_receives_win_information() {
        try (MockedStatic<GameConfiguration> mocked = Mockito.mockStatic(GameConfiguration.class)) {
            // given
            mocked.when(GameConfiguration::nextDrawDate).thenReturn(NEXT_DRAW_DATE);
            Set<Integer> userNumbers = Set.of(1, 2, 3, 4, 5, 6);
            // when
            receivedUserMessage = userChoosesNumbers(userNumbers);
            Ticket generatedTicket = Ticket.builder()
                    .hash(receivedUserMessage.getHash())
                    .numbers(userNumbers)
                    .drawDate(receivedUserMessage.getDrawDate())
                    .build();
            numbersAcceptedMessage = accepted(generatedTicket);
            // then
            assertThat(receivedUserMessage, equalTo(numbersAcceptedMessage));

            // given
            generatorDrawsWinningNumbersForDrawDay("1, 2, 3, 4, 5, 6");
            ticketsAreCheckingAndMarkingIfTheyWin(generatedTicket.getDrawDate());
            mocked.when(GameConfiguration::nextDrawDate).thenReturn(NEXT_DRAW_DATE.plusDays(1));
            // when
            checkResultMessage = userCheckResultByHash(generatedTicket.getHash());
            // then
            assertThat(checkResultMessage, equalTo(win_message()));
        }
    }

    private ResultMessageDto userChoosesNumbers(Set<Integer> numbers) {
        return numberReceiverFacade.inputNumbers(numbers);
    }

    private void generatorDrawsWinningNumbersForDrawDay(String generatedNumbers) {
        WireMock.stubFor(WireMock.get(urlPathEqualTo("/winningnumbers"))
                .willReturn(WireMock.aResponse()
                        .withBody("{\"winningNumbers\":[" + generatedNumbers + "]}")
                        .withHeader("Content-Type", "application/json")));
    }

    private void ticketsAreCheckingAndMarkingIfTheyWin(LocalDate drawDate) {
        resultCheckerFacade.checkWinnersAfterDraw(drawDate);
    }

    private String userCheckResultByHash(String generatedHash) {
        return resultAnnouncerFacade.checkResult(generatedHash);
    }
}
