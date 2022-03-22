package pl.lotto.resultannouncer;

import pl.lotto.configuration.GameConfiguration;
import pl.lotto.numberreceiver.NumberReceiverFacade;
import pl.lotto.resultchecker.ResultCheckerFacade;
import pl.lotto.resultchecker.dto.WinnersDto;

import java.time.LocalDate;

public class ResultAnnouncerFacade {

    private final ResultCheckerFacade resultCheckerFacade;
    private final NumberReceiverFacade numberReceiverFacade;

    ResultAnnouncerFacade(ResultCheckerFacade resultCheckerFacade, NumberReceiverFacade numberReceiverFacade) {
        this.resultCheckerFacade = resultCheckerFacade;
        this.numberReceiverFacade = numberReceiverFacade;
    }

    public String checkResult(String hash) {
        if (numberReceiverFacade.findByHash(hash).isEmpty()) {
            return no_ticket_message();
        }
        if (!hasDrawTookPlaceAlready(hash)) {
            return need_to_wait_message(GameConfiguration.nextDrawDate());
        }
        WinnersDto winners = resultCheckerFacade.getAllWinners();
        if (winners.getList().stream().anyMatch(e -> e.getHash().equals(hash))) {
            return win_message();
        }
        return lose_message();
    }

    private boolean hasDrawTookPlaceAlready(String hash) {
        return numberReceiverFacade.findByHash(hash).get().getDrawDate().isBefore(GameConfiguration.nextDrawDate());
    }

    public static String win_message() {
        return "You have won. Congratulations!";
    }

    public static String lose_message() {
        return "Unfortunately, You have lost. Good luck in the next game!";
    }

    public static String no_ticket_message() {
        return "Ticket associated with this hash code does not exist.";
    }

    public static String need_to_wait_message(LocalDate drawingDate) {
        return "The draw for " + drawingDate + " was not take a place yet. Check Your ticket after " + GameConfiguration.ANNOUNCER_TIME;
    }
}
