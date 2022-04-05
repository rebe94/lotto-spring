package pl.lotto.configuration;

import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;
import java.time.LocalTime;

@Getter
@Setter
public abstract class GameConfiguration {

    public static final int LOWEST_NUMBER = 1;
    public static final int HIGHEST_NUMBER = 99;
    public static final int AMOUNT_OF_NUMBERS = 6;
    public static final int RANDOM_NUMBER_BOUND = (HIGHEST_NUMBER - LOWEST_NUMBER) + 1;
    public static final LocalTime DRAW_TIME = LocalTime.of(19, 0);
    public static final LocalTime ANNOUNCER_TIME = DRAW_TIME.plusMinutes(5);
    public static final LocalTime TICKET_RECEIVER_CLOSING_TIME = DRAW_TIME.minusMinutes(15);

    public static LocalDate nextDrawDate() {
        if (TimeConfiguration.currentTime().isBefore(TICKET_RECEIVER_CLOSING_TIME)) {
            return TimeConfiguration.currentDate();
        }
        return TimeConfiguration.currentDate().plusDays(1);
    }
}
