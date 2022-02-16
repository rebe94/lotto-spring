package pl.lotto.resultannouncer;

import java.util.Set;
import pl.lotto.resultchecker.ResultCheckerFacade;

public class ResultAnnouncerFacade {

    private final ResultCheckerFacade resultCheckerFacade;

    public ResultAnnouncerFacade(ResultCheckerFacade resultCheckerFacade) {
        this.resultCheckerFacade = resultCheckerFacade;
    }

    public String checkResult(String hash) {
        resultCheckerFacade.checkWinners();
        Set<String> winners = resultCheckerFacade.getWinners();
        return winners.stream().anyMatch(e -> e.equals(hash)) ? "Winner" : "Loser";
    }
}
