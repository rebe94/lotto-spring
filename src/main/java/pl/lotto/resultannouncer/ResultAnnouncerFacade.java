package pl.lotto.resultannouncer;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import pl.lotto.resultchecker.ResultCheckerFacade;

import java.util.Set;

@Service
public class ResultAnnouncerFacade {

    private final ResultCheckerFacade resultCheckerFacade;

    @Autowired
    public ResultAnnouncerFacade(ResultCheckerFacade resultCheckerFacade) {
        this.resultCheckerFacade = resultCheckerFacade;
    }

    public String checkResult(String hash) {
        resultCheckerFacade.checkWinners();
        Set<String> winners = resultCheckerFacade.getWinners();
        return winners.stream().anyMatch(e -> e.equals(hash)) ? "Winner" : "Loser";
    }
}
