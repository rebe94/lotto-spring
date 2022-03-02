package pl.lotto.configuration;

import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;
import java.time.LocalTime;

@Getter
@Setter
public class GameConfiguration {

    public static final int LOWEST_NUMBER = 1;
    public static final int HIGHEST_NUMBER = 99;
    public static final int AMOUNT_OF_NUMBERS = 6;
    public static final int RANDOM_NUMBER_BOUND = (HIGHEST_NUMBER - LOWEST_NUMBER) + 1;
    public static final LocalTime TICKET_RECEIVER_CLOSING_TIME = LocalTime.of(18, 45);
    public static final LocalTime DRAWING_TIME = LocalTime.of(19, 0);

    public static LocalDate nextDrawingDate() {
        if (LocalTime.now().isBefore(TICKET_RECEIVER_CLOSING_TIME)) {
            return LocalDate.now();
        }
        return LocalDate.now().plusDays(1);
    }
}
