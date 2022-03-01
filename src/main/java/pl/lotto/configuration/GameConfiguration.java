package pl.lotto.configuration;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class GameConfiguration {

    public static final int LOWEST_NUMBER = 1;
    public static final int HIGHEST_NUMBER = 99;
    public static final int AMOUNT_OF_NUMBERS = 6;
    public static final int RANDOM_NUMBER_BOUND = (HIGHEST_NUMBER - LOWEST_NUMBER) + 1;
}
