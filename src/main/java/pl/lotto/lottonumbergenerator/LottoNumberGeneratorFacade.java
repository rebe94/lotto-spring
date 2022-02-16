package pl.lotto.lottonumbergenerator;

import java.util.Set;
import java.util.TreeSet;
import static pl.lotto.configuration.GameConfiguration.AMOUNT_OF_NUMBERS;
import static pl.lotto.configuration.GameConfiguration.LOWEST_NUMBER;
import static pl.lotto.configuration.GameConfiguration.RANDOM_NUMBER_BOUND;

public class LottoNumberGeneratorFacade {

    public Set<Integer> winningNumbers() {
        Set<Integer> winningNumbers = new TreeSet<>();
        while (winningNumbers.size() < AMOUNT_OF_NUMBERS) {
            int randomNumber = (int) (Math.random() * RANDOM_NUMBER_BOUND) + LOWEST_NUMBER;
            winningNumbers.add(randomNumber);
        }
        return winningNumbers;
    }
}
