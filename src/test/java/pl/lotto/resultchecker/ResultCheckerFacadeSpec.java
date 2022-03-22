package pl.lotto.resultchecker;

import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import pl.lotto.lottonumbergenerator.LottoNumberGeneratorFacade;
import pl.lotto.lottonumbergenerator.dto.WinningNumbersDto;
import pl.lotto.numberreceiver.NumberReceiverFacade;
import pl.lotto.numberreceiver.dto.TicketDto;
import pl.lotto.numberreceiver.dto.TicketsDto;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.containsInAnyOrder;
import static org.hamcrest.Matchers.empty;
import static org.hamcrest.Matchers.not;
import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.mock;
import static org.mockito.ArgumentMatchers.any;

@Tag("WithoutSpringTest")
class ResultCheckerFacadeSpec {

    private final NumberReceiverFacade numberReceiverFacade =
            mock(NumberReceiverFacade.class);
    private final LottoNumberGeneratorFacade lottoNumberGeneratorFacade =
            mock(LottoNumberGeneratorFacade.class);
    private final ResultCheckerFacade resultCheckerFacade = new ResultCheckerConfiguration()
            .resultCheckerFacadeForTests(numberReceiverFacade, lottoNumberGeneratorFacade);

    private final LocalDate DRAW_DATE = LocalDate.of(2000,1,1);
    private final List<TicketDto> ticketsDtoList = new ArrayList<>() {{
        add(new TicketDto("hash1", Set.of(1, 2, 3, 4, 5, 6), DRAW_DATE));
        add(new TicketDto("hash2", Set.of(1, 2, 3, 4, 5, 6), DRAW_DATE));
        add(new TicketDto("hash3", Set.of(1, 2, 3, 4, 5, 7), DRAW_DATE));
        add(new TicketDto("hash4", Set.of(1, 2, 3, 4, 5, 8), DRAW_DATE));
    }};
    private final TicketsDto ticketsDto = TicketsDto.builder()
            .list(ticketsDtoList)
            .build();

    @Test
    public void checks_result_and_returns_list_with_2_winners() {
        // given
        given(numberReceiverFacade.getAllTicketsByDrawDate(any())).willReturn(ticketsDto);
        final Set<Integer> winningNumbers = Set.of(1, 2, 3, 4, 5, 6);
        WinningNumbersDto winningNumbersDto = WinningNumbersDto.builder()
                .winningNumbers(winningNumbers)
                .validationMessage(WinningNumbersDto.ValidationMessage.VALID)
                .build();
        given(lottoNumberGeneratorFacade.getWinningNumbers(any())).willReturn(winningNumbersDto);

        // when
        resultCheckerFacade.checkWinnersAfterDraw(DRAW_DATE);
        List<String> winnersHash = resultCheckerFacade.getWinnersList().stream()
                .map(Winner::getHash)
                .toList();

        // then
        assertAll(
                () -> assertEquals(2, winnersHash.size()),
                () -> assertThat(winnersHash, containsInAnyOrder("hash1", "hash2"))
        );
    }

    @Test
    public void checks_result_and_returns_empty_list_without_any_winners() {
        // given
        given(numberReceiverFacade.getAllTicketsByDrawDate(any())).willReturn(ticketsDto);
        final Set<Integer> winningNumbers = Set.of(1, 2, 3, 4, 5, 99);
        WinningNumbersDto winningNumbersDto = WinningNumbersDto.builder()
                .winningNumbers(winningNumbers)
                .validationMessage(WinningNumbersDto.ValidationMessage.VALID)
                .build();
        given(lottoNumberGeneratorFacade.getWinningNumbers(any())).willReturn(winningNumbersDto);

        // when
        resultCheckerFacade.checkWinnersAfterDraw(DRAW_DATE);
        List<String> winnersHash = resultCheckerFacade.getWinnersList().stream()
                .map(Winner::getHash)
                .toList();

        // then
        assertAll(
                () -> assertEquals(0, winnersHash.size()),
                () -> assertThat(winnersHash, empty()),
                () -> assertThat(winnersHash, not(containsInAnyOrder("hash1", "hash2", "hash3", "hash4")))
        );
    }
}