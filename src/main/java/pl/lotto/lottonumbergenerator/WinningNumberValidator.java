package pl.lotto.lottonumbergenerator;

import java.util.Set;

interface WinningNumberValidator {

    boolean isNumberValid(Set<Integer> numbers);
}
