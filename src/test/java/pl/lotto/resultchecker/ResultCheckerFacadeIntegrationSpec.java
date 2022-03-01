package pl.lotto.resultchecker;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import pl.lotto.lottonumbergenerator.LottoNumberGeneratorFacade;
import pl.lotto.numberreceiver.NumberReceiverFacade;
import pl.lotto.numberreceiver.Ticket;

import java.util.HashSet;
import java.util.Set;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.containsInAnyOrder;
import static org.hamcrest.Matchers.empty;
import static org.hamcrest.Matchers.not;
import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.BDDMockito.given;

@SpringBootTest
class ResultCheckerFacadeIntegrationSpec {

    @MockBean
    private NumberReceiverFacade numberReceiverFacade;
    @MockBean
    private LottoNumberGeneratorFacade lottoNumberGeneratorFacade;

    @Autowired
    private ResultCheckerFacade resultCheckerFacade = new ResultCheckerConfiguration()
            .resultCheckerFacadeForTests(numberReceiverFacade, lottoNumberGeneratorFacade);

    Set<Ticket> ticketEntities = new HashSet<>() {{
        add(new Ticket("hash1", Set.of(1, 2, 3, 4, 5, 6)));
        add(new Ticket("hash2", Set.of(1, 2, 3, 4, 5, 6)));
        add(new Ticket("hash3", Set.of(1, 2, 3, 4, 5, 7)));
        add(new Ticket("hash4", Set.of(1, 2, 3, 4, 5, 8)));
        }};

    @Test
    @DisplayName("module should give a list of 2 winners")
    public void check_result_and_return_list_with_2_winners() {
        // given
        given(numberReceiverFacade.getAllTickets())
                .willReturn(ticketEntities);
        final Set<Integer> winningNumbers = Set.of(1, 2, 3, 4, 5, 6);
        given(lottoNumberGeneratorFacade.getWinningNumbers()).willReturn(winningNumbers);

        // when
        resultCheckerFacade.checkWinners();
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
        given(numberReceiverFacade.getAllTickets())
                .willReturn(ticketEntities);
        final Set<Integer> winningNumbers = Set.of(1, 2, 3, 4, 5, 99);
        given(lottoNumberGeneratorFacade.getWinningNumbers()).willReturn(winningNumbers);

        // when
        resultCheckerFacade.checkWinners();
        Set<String> winners = resultCheckerFacade.getWinners();

        // then
        assertAll(
                () -> assertEquals(0, winners.size()),
                () -> assertThat(winners, empty()),
                () -> assertThat(winners, not(containsInAnyOrder("hash1", "hash2", "hash3", "hash4")))
        );
    }
}