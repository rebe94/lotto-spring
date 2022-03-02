package pl.lotto.numberreceiver;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.time.LocalDate;
import java.util.Set;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;

@Tag("SpringTest")
@SpringBootTest
class NumberReceiverFacadeIntegrationSpec {

    @Autowired
    private NumberReceiverFacade numberReceiverFacade;

    private final ResultMessage not_accepted = new ResultMessage
            ("Wrong amount of numbers or numbers out of range", "False", "False");

    @Test
    @DisplayName("module should accept when user gave exactly 6 numbers in range")
    public void receive_six_numbers_and_return_they_are_accepted() {
        // when
        final ResultMessage result = numberReceiverFacade.inputNumbers(Set.of(1, 2, 3, 4, 5, 6));
        final String SOME_HASH = result.getHash();
        final String SOME_DATE = result.getDrawingDate();

        // then
        ResultMessage accepted = new ResultMessage("Accepted", SOME_HASH, SOME_DATE);
        assertThat(result, equalTo(accepted));
    }

    @Test
    @DisplayName("module should not accept when user gave less than 6 numbers")
    public void receive_5_numbers_and_return_they_are_falsed() {
        // when
        ResultMessage result = numberReceiverFacade.inputNumbers(Set.of(1, 2, 3, 4, 5));

        // then
        assertThat(result, equalTo(not_accepted));
    }

    @Test
    @DisplayName("module should not accept when user gave more than 6 numbers")
    public void receive_7_numbers_and_return_they_are_falsed() {
        // when
        ResultMessage result = numberReceiverFacade.inputNumbers(Set.of(1, 2, 3, 4, 5, 6, 7));

        // then
        assertThat(result, equalTo(not_accepted));
    }

    @Test
    @DisplayName("module should not accept when user gave number out of range")
    public void receive_6_numbers_out_of_range_and_return_they_are_falsed() {
        // when
        ResultMessage result = numberReceiverFacade.inputNumbers(Set.of(1, 2, 3, 4, 5, 100));

        // then
        assertThat(result, equalTo(not_accepted));
    }
}
