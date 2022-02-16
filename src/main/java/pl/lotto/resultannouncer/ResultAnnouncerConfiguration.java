package pl.lotto.resultannouncer;

import pl.lotto.resultchecker.ResultCheckerFacade;

public class ResultAnnouncerConfiguration {

    public ResultAnnouncerFacade resultAnnouncerFacade(ResultCheckerFacade resultCheckerFacade) {
        return new ResultAnnouncerFacade(resultCheckerFacade);
    }
}
