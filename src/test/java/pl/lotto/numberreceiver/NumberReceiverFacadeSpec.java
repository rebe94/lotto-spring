package pl.lotto.numberreceiver;

import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import pl.lotto.numberreceiver.dto.ResultMessageDto;

import java.util.Collections;
import java.util.Set;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;

@Tag("WithoutSpringTest")
class NumberReceiverFacadeSpec {

    private final NumberReceiverFacade numberReceiverFacade = new NumberReceiverConfiguration()
            .numberReceiverFacadeForTests();

    private final ResultMessageDto not_accepted = NumberReceiverMessageProvider.failed();

    @Test
    public void receives_6_numbers_and_returns_accepted_message() {
        // given
        final Set<Integer> userNumbers = Set.of(1, 2, 3, 4, 5, 6);
        // when
        ResultMessageDto result = numberReceiverFacade.inputNumbers(userNumbers);
        Ticket generatedTicket = Ticket.builder()
                .hash(result.getHash())
                .numbers(userNumbers)
                .drawDate(result.getDrawDate())
                .build();
        ResultMessageDto accepted = NumberReceiverMessageProvider.accepted(generatedTicket);

        // then
        assertThat(result, equalTo(accepted));
    }

    @Test
    public void receives_5_numbers_and_returns_failed_message() {
        // given
        final Set<Integer> userNumbers = Set.of(1, 2, 3, 4, 5);
        // when
        ResultMessageDto result = numberReceiverFacade.inputNumbers(userNumbers);

        // then
        assertThat(result, equalTo(not_accepted));
    }

    @Test
    public void receives_7_numbers_and_returns_failed_message() {
        // given
        final Set<Integer> userNumbers = Set.of(1, 2, 3, 4, 5, 6, 7);
        // when
        ResultMessageDto result = numberReceiverFacade.inputNumbers(userNumbers);

        // then
        assertThat(result, equalTo(not_accepted));
    }

    @Test
    public void receives_6_numbers_out_of_range_and_returns_failed_message() {
        // given
        final Set<Integer> userNumbers = Set.of(1, 2, 3, 4, 5, 100);
        // when
        ResultMessageDto result = numberReceiverFacade.inputNumbers(userNumbers);

        // then
        assertThat(result, equalTo(not_accepted));
    }

    @Test
    public void receives_no_numbers_and_returns_input_error_message() {
        // given
        final ResultMessageDto input_error = NumberReceiverMessageProvider.input_error();
        Set<Integer> userNumbers = Collections.emptySet();
        // when
        ResultMessageDto result = numberReceiverFacade.inputNumbers(userNumbers);

        // then
        assertThat(result, equalTo(input_error));
    }
}
