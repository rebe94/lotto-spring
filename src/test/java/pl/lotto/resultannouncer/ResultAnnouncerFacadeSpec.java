package pl.lotto.resultannouncer;

import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import org.mockito.MockedStatic;
import org.mockito.Mockito;
import pl.lotto.configuration.GameConfiguration;
import pl.lotto.numberreceiver.NumberReceiverFacade;
import pl.lotto.numberreceiver.Ticket;
import pl.lotto.resultchecker.ResultCheckerFacade;
import pl.lotto.resultchecker.dto.WinnerDto;
import pl.lotto.resultchecker.dto.WinnersDto;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.Set;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.mock;

@Tag("WithoutSpringTest")
class ResultAnnouncerFacadeSpec {

    private final NumberReceiverFacade numberReceiverFacade = mock(NumberReceiverFacade.class);
    private final ResultCheckerFacade resultCheckerFacade = mock(ResultCheckerFacade.class);
    private final ResultAnnouncerFacade resultAnnouncerFacade = new ResultAnnouncerConfiguration()
            .resultAnnouncerFacade(resultCheckerFacade, numberReceiverFacade);

    //TODO czy finalne zmienne referencyjne powinny być pisane z dużych liter?
    private final Set<Integer> WIN_NUMBERS = Set.of(1, 2, 3, 4, 5, 6);
    private final Set<Integer> LOSE_NUMBERS = Set.of(1, 2, 3, 4, 5, 10);
    private final Set<Integer> SOME_NUMBERS = Set.of(11, 12, 13, 14, 15, 16);
    private final LocalDate DRAW_DATE = LocalDate.of(2000, 1, 1);
    private final String WIN_HASH = "win_hash";
    private final String LOSE_HASH = "lose_hash";
    private final String NOT_EXISTING_HASH_CODE = "not_existing_ticket";
    private final String SOME_HASH = "some_hash";
    private final Ticket WIN_TICKET = new Ticket(WIN_HASH, WIN_NUMBERS, DRAW_DATE);
    private final Ticket LOSE_TICKET = new Ticket(LOSE_HASH, LOSE_NUMBERS, DRAW_DATE);
    private final Ticket BEFORE_DRAW_TICKET = new Ticket(SOME_HASH, SOME_NUMBERS, DRAW_DATE);
    private final List<WinnerDto> winnersDtoList = new ArrayList<>() {{
        add(new WinnerDto(WIN_TICKET.getHash(), WIN_TICKET.getNumbers(), WIN_TICKET.getDrawDate()));
    }};
    private final WinnersDto winnersDto = WinnersDto.builder()
            .list(winnersDtoList)
            .build();

    @Test
    public void returns_win_message_when_hash_is_found_as_a_winner() {
        // given
        given(numberReceiverFacade.findByHash(WIN_TICKET.getHash())).willReturn(Optional.of(WIN_TICKET));
        given(resultCheckerFacade.getAllWinners()).willReturn(winnersDto);
        // when
        String result = resultAnnouncerFacade.checkResult(WIN_TICKET.getHash());
        String win_message = ResultAnnouncerFacade.win_message();

        //then
        assertThat(result, equalTo(win_message));
    }

    @Test
    public void returns_lost_message_when_hash_is_not_found_as_a_winner() {
        // given
        given(numberReceiverFacade.findByHash(LOSE_TICKET.getHash())).willReturn(Optional.of(LOSE_TICKET));
        given(resultCheckerFacade.getAllWinners()).willReturn(winnersDto);
        // when
        String result = resultAnnouncerFacade.checkResult(LOSE_TICKET.getHash());
        String lose_message = ResultAnnouncerFacade.lose_message();

        //then
        assertThat(result, equalTo(lose_message));
    }

    @Test
    public void returns_no_ticket_message_when_hash_does_not_exist() {
        // given
        given(numberReceiverFacade.findByHash(NOT_EXISTING_HASH_CODE)).willReturn(Optional.empty());
        // when
        String result = resultAnnouncerFacade.checkResult(NOT_EXISTING_HASH_CODE);
        String no_ticket_message = ResultAnnouncerFacade.no_ticket_message();

        //then
        assertThat(result, equalTo(no_ticket_message));
    }

    @Test
    public void returns_need_to_wait_message_when_checks_hash_code_before_draw() {
        // given
        try (MockedStatic<GameConfiguration> mocked = Mockito.mockStatic(GameConfiguration.class)) {
            mocked.when(GameConfiguration::nextDrawDate).thenReturn(DRAW_DATE);
            given(numberReceiverFacade.findByHash(BEFORE_DRAW_TICKET.getHash())).willReturn(Optional.of(BEFORE_DRAW_TICKET));
            // when
            String result = resultAnnouncerFacade.checkResult(BEFORE_DRAW_TICKET.getHash());
            String need_to_wait_message = ResultAnnouncerFacade.need_to_wait_message(BEFORE_DRAW_TICKET.getDrawDate());

            //then
            assertThat(result, equalTo(need_to_wait_message));
        }
    }

    @Test
    public void areturns_need_to_wait_message_when_checks_hash_code_before_draw() {
        // given
        try (MockedStatic<GameConfiguration> mocked = Mockito.mockStatic(GameConfiguration.class)) {
            mocked.when(GameConfiguration::nextDrawDate).thenReturn(DRAW_DATE);
            given(numberReceiverFacade.findByHash(BEFORE_DRAW_TICKET.getHash())).willReturn(Optional.of(BEFORE_DRAW_TICKET));
            // when
            String result = resultAnnouncerFacade.checkResult(BEFORE_DRAW_TICKET.getHash());
            String need_to_wait_message = ResultAnnouncerFacade.need_to_wait_message(BEFORE_DRAW_TICKET.getDrawDate());

            //then
            assertThat(result, equalTo(need_to_wait_message));
        }
    }
}