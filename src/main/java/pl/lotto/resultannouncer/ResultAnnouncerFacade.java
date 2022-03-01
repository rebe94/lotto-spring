package pl.lotto.resultannouncer;

import java.util.Set;

import org.springframework.scheduling.annotation.Scheduled;
import pl.lotto.resultchecker.ResultCheckerFacade;

public class ResultAnnouncerFacade {

    private final ResultCheckerFacade resultCheckerFacade;

    ResultAnnouncerFacade(ResultCheckerFacade resultCheckerFacade) {
        this.resultCheckerFacade = resultCheckerFacade;
    }

    public String checkResult(String hash) {
        Set<String> winners = resultCheckerFacade.getWinners();
        return winners.stream().anyMatch(e -> e.equals(hash)) ? "Winner" : "Loser";
    }
}
