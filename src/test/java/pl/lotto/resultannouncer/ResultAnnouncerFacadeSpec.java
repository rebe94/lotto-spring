package pl.lotto.resultannouncer;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import pl.lotto.resultchecker.ResultCheckerFacade;
import pl.lotto.resultchecker.dto.WinnerDto;
import pl.lotto.resultchecker.dto.WinnersDto;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.mock;

@Tag("WithoutSpringTest")
class ResultAnnouncerFacadeSpec {

    private final ResultCheckerFacade resultCheckerFacade = mock(ResultCheckerFacade.class);
    private final ResultAnnouncerFacade resultAnnouncerFacade = new ResultAnnouncerConfiguration()
            .resultAnnouncerFacade(resultCheckerFacade);

    final Set<Integer> SOME_NUMBERS = Set.of(1,2,3,4,5,6);
    final LocalDate SOME_DATE = LocalDate.of(2000, 1, 1);
    private final List<WinnerDto> winnersDtoList = new ArrayList<>() {{
        add(new WinnerDto("hash1", SOME_NUMBERS, SOME_DATE));
        add(new WinnerDto("hash2", SOME_NUMBERS, SOME_DATE));
        add(new WinnerDto("hash3", SOME_NUMBERS, SOME_DATE));
    }};
    private final WinnersDto winnersDto = WinnersDto.builder()
            .list(winnersDtoList)
            .build();

    @Test
    @DisplayName("Should return win message when hash is found as a winner")
    public void return_win_message_when_hash_is_found_as_a_winner() {
        // given
        given(resultCheckerFacade.getAllWinners()).willReturn(winnersDto);

        // when
        String win_message = resultAnnouncerFacade.checkResult("hash1");

        //then
        assertEquals("Winner", win_message);
    }

    @Test
    @DisplayName("Should return lost message when hash is not found as a winner")
    public void return_lost_message_when_hash_is_not_found_as_a_winner() {
        // given
        given(resultCheckerFacade.getAllWinners()).willReturn(winnersDto);

        // when
        String lost_message = resultAnnouncerFacade.checkResult("hash50");

        //then
        assertEquals("Loser", lost_message);
    }

}