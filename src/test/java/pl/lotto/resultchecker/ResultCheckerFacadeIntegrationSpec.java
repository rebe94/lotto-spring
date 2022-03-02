package pl.lotto.resultchecker;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import pl.lotto.lottonumbergenerator.LottoNumberGeneratorFacade;
import pl.lotto.numberreceiver.NumberReceiverFacade;
import pl.lotto.numberreceiver.Ticket;

import java.time.LocalDate;
import java.util.HashSet;
import java.util.Set;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.containsInAnyOrder;
import static org.hamcrest.Matchers.empty;
import static org.hamcrest.Matchers.not;
import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.BDDMockito.given;

@Tag("SpringTest")
@SpringBootTest
class ResultCheckerFacadeIntegrationSpec {

    @MockBean
    private NumberReceiverFacade numberReceiverFacade;
    @MockBean
    private LottoNumberGeneratorFacade lottoNumberGeneratorFacade;

    @Autowired
    private ResultCheckerFacade resultCheckerFacade = new ResultCheckerConfiguration()
            .resultCheckerFacadeForTests(numberReceiverFacade, lottoNumberGeneratorFacade);

    private final LocalDate SOME_DATE = LocalDate.of(2000,1,1);
    private final Set<Ticket> tickets = new HashSet<>() {{
        add(new Ticket("hash1", Set.of(1, 2, 3, 4, 5, 6), SOME_DATE));
        add(new Ticket("hash2", Set.of(1, 2, 3, 4, 5, 6), SOME_DATE));
        add(new Ticket("hash3", Set.of(1, 2, 3, 4, 5, 7), SOME_DATE));
        add(new Ticket("hash4", Set.of(1, 2, 3, 4, 5, 8), SOME_DATE));
        }};

    @Test
    @DisplayName("module should give a list of 2 winners")
    public void check_result_and_return_list_with_2_winners() {
        // given
        given(numberReceiverFacade.getAllTicketsByDrawingDate(SOME_DATE))
                .willReturn(tickets);
        final Set<Integer> winningNumbers = Set.of(1, 2, 3, 4, 5, 6);
        given(lottoNumberGeneratorFacade.getWinningNumbers(SOME_DATE)).willReturn(winningNumbers);

        // when
        resultCheckerFacade.checkWinnersAfterDraw();
        Set<String> winners = resultCheckerFacade.getWinners();

        // then
        assertAll(
                () -> assertEquals(2, winners.size()),
                () -> assertThat(winners, containsInAnyOrder("hash1", "hash2"))
        );
    }

    @Test
    @DisplayName("module should give an empty list without any winners")
    public void check_result_and_return_empty_list_without_any_winners() {
        // given
        given(numberReceiverFacade.getAllTicketsByDrawingDate(SOME_DATE))
                .willReturn(tickets);
        final Set<Integer> winningNumbers = Set.of(1, 2, 3, 4, 5, 99);
        given(lottoNumberGeneratorFacade.getWinningNumbers(SOME_DATE)).willReturn(winningNumbers);

        // when
        resultCheckerFacade.checkWinnersAfterDraw();
        Set<String> winners = resultCheckerFacade.getWinners();

        // then
        assertAll(
                () -> assertEquals(0, winners.size()),
                () -> assertThat(winners, empty()),
                () -> assertThat(winners, not(containsInAnyOrder("hash1", "hash2", "hash3", "hash4")))
        );
    }
}