package pl.lotto;

import com.github.tomakehurst.wiremock.WireMockServer;
import com.github.tomakehurst.wiremock.client.WireMock;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import org.springframework.util.SocketUtils;
import pl.lotto.configuration.TimeConfiguration;
import pl.lotto.lottonumbergenerator.LottoNumberGeneratorConfiguration;
import pl.lotto.lottonumbergenerator.LottoNumberGeneratorFacade;
import pl.lotto.numberreceiver.NumberReceiverConfiguration;
import pl.lotto.numberreceiver.NumberReceiverFacade;
import pl.lotto.numberreceiver.Ticket;
import pl.lotto.numberreceiver.dto.ResultMessageDto;
import pl.lotto.resultannouncer.ResultAnnouncerConfiguration;
import pl.lotto.resultannouncer.ResultAnnouncerFacade;
import pl.lotto.resultchecker.ResultCheckerConfiguration;
import pl.lotto.resultchecker.ResultCheckerFacade;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.Set;

import static com.github.tomakehurst.wiremock.client.WireMock.urlPathEqualTo;
import static com.github.tomakehurst.wiremock.core.WireMockConfiguration.options;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;
import static pl.lotto.numberreceiver.NumberReceiverMessageProvider.accepted;
import static pl.lotto.resultannouncer.ResultAnnouncerMessageProvider.lose_message;
import static pl.lotto.resultannouncer.ResultAnnouncerMessageProvider.no_ticket_message;
import static pl.lotto.resultannouncer.ResultAnnouncerMessageProvider.win_message;

@Tag("WithoutSpringTest")
class SimpleIntegrationSpec {

    private final int port = SocketUtils.findAvailableTcpPort();
    private final String urlServiceForTest = "http://localhost:" + port + "/";
    private final LocalDate DRAW_DATE = LocalDate.of(2000, 1, 1);
    private final LocalTime TIME_BEFORE_CLOSING_TIME = LocalTime.of(18, 30);
    private final LocalTime TIME_AFTER_ANNOUNCEMENT = LocalTime.of(19, 15);


    private final LottoNumberGeneratorFacade lottoNumberGeneratorFacade = new LottoNumberGeneratorConfiguration()
            .lottoNumberGeneratorFacadeForTests(urlServiceForTest);
    private final NumberReceiverFacade numberReceiverFacade = new NumberReceiverConfiguration().numberReceiverFacadeForTests();
    private final ResultCheckerFacade resultCheckerFacade = new ResultCheckerConfiguration()
            .resultCheckerFacadeForTests(numberReceiverFacade, lottoNumberGeneratorFacade);
    private final ResultAnnouncerFacade resultAnnouncerFacade = new ResultAnnouncerConfiguration()
            .resultAnnouncerFacade(resultCheckerFacade, numberReceiverFacade);
    private WireMockServer wireMockServer;

    private ResultMessageDto receivedUserMessage;
    private ResultMessageDto numbersAcceptedMessage;
    private String checkResultMessage;

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
        TimeConfiguration.useMockTime(DRAW_DATE.atTime(TIME_BEFORE_CLOSING_TIME), TimeConfiguration.getRealTimeZone().toZoneId());
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
        TimeConfiguration.useMockTime(DRAW_DATE.atTime(TIME_AFTER_ANNOUNCEMENT), TimeConfiguration.getRealTimeZone().toZoneId());
        // when
        checkResultMessage = userCheckResultByHash(generatedTicket.getHash());
        // then
        assertThat(checkResultMessage, equalTo(win_message()));
    }

    @Test
    public void user_chooses_correct_numbers_and_receives_acceptance_and_hash_code_then_checks_result_and_receives_lose_information() {
        // given
        TimeConfiguration.useMockTime(DRAW_DATE.atTime(TIME_BEFORE_CLOSING_TIME), TimeConfiguration.getRealTimeZone().toZoneId());
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
        generatorDrawsWinningNumbersForDrawDay("1, 2, 3, 4, 5, 15");
        ticketsAreCheckingAndMarkingIfTheyWin(generatedTicket.getDrawDate());
        TimeConfiguration.useMockTime(DRAW_DATE.atTime(TIME_AFTER_ANNOUNCEMENT), TimeConfiguration.getRealTimeZone().toZoneId());
        // when
        checkResultMessage = userCheckResultByHash(generatedTicket.getHash());
        // then
        assertThat(checkResultMessage, equalTo(lose_message()));
    }

    @Test
    public void user_checks_result_of_ticket_with_wrong_hash_code_and_receives_no_ticket_information() {
        // given
        // when
        checkResultMessage = userCheckResultByHash("not_existing_hash_code");
        // then
        assertThat(checkResultMessage, equalTo(no_ticket_message()));
    }

    private ResultMessageDto userChoosesNumbers(Set<Integer> numbers) {
        return numberReceiverFacade.inputNumbers(numbers);
    }

    private void generatorDrawsWinningNumbersForDrawDay(String generatedNumbers) {
        WireMock.stubFor(WireMock.get(urlPathEqualTo("/winningnumbers"))
                .willReturn(WireMock.aResponse()
                        .withBody("{\"winningNumbers\" : [" + generatedNumbers + "]}")
                        .withHeader("Content-Type", "application/json")));
    }

    private void ticketsAreCheckingAndMarkingIfTheyWin(LocalDate drawDate) {
        resultCheckerFacade.checkWinnersAfterDraw(drawDate);
    }

    private String userCheckResultByHash(String generatedHash) {
        return resultAnnouncerFacade.checkResult(generatedHash);
    }
}