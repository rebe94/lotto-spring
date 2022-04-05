package pl.lotto.resultannouncer;

import pl.lotto.configuration.TimeConfiguration;
import pl.lotto.numberreceiver.NumberReceiverFacade;
import pl.lotto.numberreceiver.Ticket;
import pl.lotto.resultchecker.ResultCheckerFacade;
import pl.lotto.resultchecker.dto.WinnersDto;

import java.time.LocalDateTime;

import static pl.lotto.configuration.GameConfiguration.ANNOUNCER_TIME;
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
        Ticket ticket = numberReceiverFacade.findByHash(hash).get();
        if (!isAfterAnnouncerTime(ticket)) {
            return need_to_wait_message(ticket.getDrawDate());
        }
        WinnersDto winners = resultCheckerFacade.getAllWinners();
        if (winners.getList().stream().anyMatch(e -> e.getHash().equals(hash))) {
            return win_message();
        }
        return lose_message();
    }

    private boolean isAfterAnnouncerTime(Ticket ticket) {
        LocalDateTime announcerTimeForGivenTicket = LocalDateTime.of(ticket.getDrawDate(), ANNOUNCER_TIME);
        return TimeConfiguration.currentDateTime().isAfter(announcerTimeForGivenTicket);
    }
}
