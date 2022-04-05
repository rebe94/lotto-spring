package pl.lotto.configuration;

import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.greaterThanOrEqualTo;
import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static pl.lotto.configuration.GameConfiguration.AMOUNT_OF_NUMBERS;
import static pl.lotto.configuration.GameConfiguration.ANNOUNCER_TIME;
import static pl.lotto.configuration.GameConfiguration.DRAW_TIME;
import static pl.lotto.configuration.GameConfiguration.HIGHEST_NUMBER;
import static pl.lotto.configuration.GameConfiguration.LOWEST_NUMBER;
import static pl.lotto.configuration.GameConfiguration.TICKET_RECEIVER_CLOSING_TIME;

@Tag("WithoutSpringTest")
class GameConfigurationTest {

    @Test
    public void numbers_configuration_should_be_determined_correctly_for_proper_application_working() {
        assertThat((HIGHEST_NUMBER - LOWEST_NUMBER), greaterThanOrEqualTo(AMOUNT_OF_NUMBERS));
    }

    @Test
    public void time_configuration_should_be_determined_correctly_for_proper_application_working() {
        assertAll(
                () -> assertTrue(TICKET_RECEIVER_CLOSING_TIME.isBefore(DRAW_TIME)),
                () -> assertTrue(ANNOUNCER_TIME.isAfter(DRAW_TIME))
        );
    }
}