package pl.lotto.resultannouncer;

import pl.lotto.resultchecker.ResultCheckerFacade;
import pl.lotto.resultchecker.dto.WinnersDto;

public class ResultAnnouncerFacade {

    private final ResultCheckerFacade resultCheckerFacade;

    ResultAnnouncerFacade(ResultCheckerFacade resultCheckerFacade) {
        this.resultCheckerFacade = resultCheckerFacade;
    }

    public String checkResult(String hash) {
        WinnersDto winners = resultCheckerFacade.getAllWinners();
        return winners.getList().stream().anyMatch(e -> e.getHash().equals(hash)) ? "Winner" : "Loser";
    }
}
