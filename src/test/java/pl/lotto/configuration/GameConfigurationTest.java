package pl.lotto.configuration;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.greaterThanOrEqualTo;
import static pl.lotto.configuration.GameConfiguration.AMOUNT_OF_NUMBERS;
import static pl.lotto.configuration.GameConfiguration.HIGHEST_NUMBER;
import static pl.lotto.configuration.GameConfiguration.LOWEST_NUMBER;

@Tag("WithoutSpringTest")
class GameConfigurationTest {

    @Test
    @DisplayName("configuration numbers should be determined correctly to proper application working")
    void configuration_numbers_should_be_determined_correctly_to_proper_application_working() {
        assertThat((HIGHEST_NUMBER-LOWEST_NUMBER), greaterThanOrEqualTo(AMOUNT_OF_NUMBERS));
    }
}