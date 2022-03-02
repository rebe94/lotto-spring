package pl.lotto.resultchecker;

import java.time.LocalDate;
import java.util.Set;

import org.springframework.scheduling.annotation.Scheduled;
import pl.lotto.lottonumbergenerator.LottoNumberGeneratorFacade;
import pl.lotto.numberreceiver.NumberReceiverFacade;
import pl.lotto.numberreceiver.Ticket;

public class ResultCheckerFacade {

    private final WinnerRepository winnerRepository;
    private final NumberReceiverFacade numberReceiverFacade;
    private final LottoNumberGeneratorFacade lottoNumberGeneratorFacade;

    ResultCheckerFacade(WinnerRepository winnerRepository,
                        NumberReceiverFacade numberReceiverFacade,
                        LottoNumberGeneratorFacade lottoNumberGeneratorFacade) {
        this.winnerRepository = winnerRepository;
        this.numberReceiverFacade = numberReceiverFacade;
        this.lottoNumberGeneratorFacade = lottoNumberGeneratorFacade;
    }

    public Set<String> getWinners() {
        return winnerRepository.getAllWinners();
    }

    @Scheduled(cron = "*/30 * * * * *")
    //@Scheduled(cron = "0 15 19 * * *")
    public void checkWinnersAfterDraw() {
        Set<Integer> winningNumbers = lottoNumberGeneratorFacade.getWinningNumbers(LocalDate.now());
        Iterable<Ticket> tickets = numberReceiverFacade.getAllTicketsByDrawingDate(LocalDate.now());
        tickets.forEach(t -> {
            if (t.getNumbers().equals(winningNumbers)) {
                winnerRepository.save(); //TODO
            }
        });
    }
}
