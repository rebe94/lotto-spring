package pl.lotto.lottonumbergenerator;

import java.util.Collections;
import java.util.Set;

import static pl.lotto.configuration.GameConfiguration.AMOUNT_OF_NUMBERS;
import static pl.lotto.configuration.GameConfiguration.HIGHEST_NUMBER;
import static pl.lotto.configuration.GameConfiguration.LOWEST_NUMBER;

class WinningNumberValidatorImpl implements WinningNumberValidator {

    public boolean isNumberValid(Set<Integer> numbers) {
        return numbers.size() == AMOUNT_OF_NUMBERS && isNumberInRange(numbers);
    }

    private boolean isNumberInRange(Set<Integer> numbers) {
        Integer max = Collections.max(numbers);
        Integer min = Collections.min(numbers);
        return min >= LOWEST_NUMBER && max <= HIGHEST_NUMBER;
    }
}
