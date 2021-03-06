package pl.lotto.resultannouncer;

import pl.lotto.configuration.GameConfiguration;

import java.time.LocalDate;

public class ResultAnnouncerMessageProvider {

    public static String win_message() {
        return "You have won. Congratulations!";
    }

    public static String lose_message() {
        return "Unfortunately, You have lost. Good luck in the next game!";
    }

    public static String no_ticket_message() {
        return "Ticket associated with this hash code does not exist.";
    }

    public static String need_to_wait_message(LocalDate drawDate) {
        return "Results of draw for " + drawDate + " will be available after " + GameConfiguration.ANNOUNCER_TIME + ". Check Your result later.";
    }
}
