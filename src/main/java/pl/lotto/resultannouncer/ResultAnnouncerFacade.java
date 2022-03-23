package pl.lotto.resultannouncer;

import pl.lotto.configuration.GameConfiguration;
import pl.lotto.numberreceiver.NumberReceiverFacade;
import pl.lotto.resultchecker.ResultCheckerFacade;
import pl.lotto.resultchecker.dto.WinnersDto;

import java.time.LocalDate;

import static pl.lotto.resultannouncer.ResultAnnouncerMessageProvider.lose_message;
import static pl.lotto.resultannouncer.ResultAnnouncerMessageProvider.need_to_wait_message;
import static pl.lotto.resultannouncer.ResultAnnouncerMessageProvider.no_ticket_message;
import static pl.lotto.resultannouncer.ResultAnnouncerMessageProvider.win_message;

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
}
