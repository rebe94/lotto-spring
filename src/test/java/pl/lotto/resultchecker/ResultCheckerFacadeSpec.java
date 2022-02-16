package pl.lotto.resultchecker;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import pl.lotto.lottonumbergenerator.LottoNumberGeneratorFacade;
import pl.lotto.numberreceiver.NumberReceiverFacade;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.containsInAnyOrder;
import static org.hamcrest.Matchers.empty;
import static org.hamcrest.Matchers.not;
import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.mock;

class ResultCheckerFacadeSpec {

    private final NumberReceiverFacade numberReceiverFacade =
            mock(NumberReceiverFacade.class);
    private final LottoNumberGeneratorFacade lottoNumberGeneratorFacade =
            mock(LottoNumberGeneratorFacade.class);
    ResultCheckerFacade resultCheckerFacade = new ResultCheckerConfiguration()
            .resultCheckerFacade(new InMemoryWinnersRepository(), numberReceiverFacade, lottoNumberGeneratorFacade);
    Map<String, Set<Integer>> usersNumbers = new HashMap<>() {{
            put("hash1", Set.of(1, 2, 3, 4, 5, 6));
            put("hash2", Set.of(1, 2, 3, 4, 5, 6));
            put("hash3", Set.of(1, 2, 3, 4, 5, 7));
            put("hash4", Set.of(1, 2, 3, 4, 5, 8));
        }};

    @Test
    @DisplayName("module should give a list of 2 winners")
    public void check_result_and_return_list_with_2_winners() {
        // given
        given(numberReceiverFacade.allNumbersFromUsers())
                .willReturn(usersNumbers);
        Set<Integer> winningNumbers = Set.of(1, 2, 3, 4, 5, 6);
        given(lottoNumberGeneratorFacade.winningNumbers())
                .willReturn(winningNumbers);
        System.out.println("Po nadpisaniu metody w testach: " + lottoNumberGeneratorFacade.winningNumbers());

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
        given(numberReceiverFacade.allNumbersFromUsers())
                .willReturn(usersNumbers);
        Set<Integer> winningNumbers = Set.of(1, 2, 3, 4, 5, 99);
        given(lottoNumberGeneratorFacade.winningNumbers())
                .willReturn(winningNumbers);
        System.out.println("Po nadpisaniu metody w testach: " + lottoNumberGeneratorFacade.winningNumbers());

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