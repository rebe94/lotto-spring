package pl.lotto.resultannouncer;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import pl.lotto.resultchecker.ResultCheckerFacade;

import java.util.HashSet;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.mock;

class ResultAnnouncerFacadeSpec {

    final ResultCheckerFacade resultCheckerFacade = mock(ResultCheckerFacade.class);
    final ResultAnnouncerFacade resultAnnouncerFacade = new ResultAnnouncerFacade(resultCheckerFacade);
    Set<String> winners = new HashSet<>() {{
        add("hash1");
        add("hash2");
        add("hash3");
    }};

    @Test
    @DisplayName("Should return win message when hash is found as a winner")
    public void return_win_message_when_hash_is_found_as_a_winner() {
        // given
        given(resultCheckerFacade.getWinners()).willReturn(winners);

        // when
        String win_message = resultAnnouncerFacade.checkResult("hash1");

        //then
        assertEquals("Winner", win_message);
    }

    @Test
    @DisplayName("Should return lost message when hash is not found as a winner")
    public void return_lost_message_when_hash_is_not_found_as_a_winner() {
        // given
        given(resultCheckerFacade.getWinners()).willReturn(winners);

        // when
        String lost_message = resultAnnouncerFacade.checkResult("hash50");

        //then
        assertEquals("Loser", lost_message);
    }

}