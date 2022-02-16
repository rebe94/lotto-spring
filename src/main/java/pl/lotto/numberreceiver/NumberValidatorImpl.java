package pl.lotto.numberreceiver;

import java.util.Collections;
import java.util.Set;
import static pl.lotto.configuration.GameConfiguration.AMOUNT_OF_NUMBERS;
import static pl.lotto.configuration.GameConfiguration.HIGHEST_NUMBER;
import static pl.lotto.configuration.GameConfiguration.LOWEST_NUMBER;

public class NumberValidatorImpl implements NumberValidator {

    public boolean numbersAreValid(Set<Integer> numbers) {
        return numbers.size() == AMOUNT_OF_NUMBERS && isNumberInRange(numbers);
    }

    private boolean isNumberInRange(Set<Integer> numbers) {
        Integer max = Collections.max(numbers);
        Integer min = Collections.min(numbers);
        return min >= LOWEST_NUMBER && max <= HIGHEST_NUMBER;
    }
}
