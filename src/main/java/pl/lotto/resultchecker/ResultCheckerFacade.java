package pl.lotto.resultchecker;

import java.util.HashSet;
import java.util.Set;

import pl.lotto.lottonumbergenerator.LottoNumberGeneratorFacade;
import pl.lotto.numberreceiver.NumberReceiverFacade;
import pl.lotto.numberreceiver.Ticket;

public class ResultCheckerFacade {

    private final WinnersRepository winnersRepository;
    private final NumberReceiverFacade numberReceiverFacade;
    private final LottoNumberGeneratorFacade lottoNumberGeneratorFacade;

    ResultCheckerFacade(WinnersRepository winnersRepository,
                               NumberReceiverFacade numberReceiverFacade,
                               LottoNumberGeneratorFacade lottoNumberGeneratorFacade) {
        this.winnersRepository = winnersRepository;
        this.numberReceiverFacade = numberReceiverFacade;
        this.lottoNumberGeneratorFacade = lottoNumberGeneratorFacade;
    }

    public Set<String> getWinners() {
        return winnersRepository.getAllWinners();
    }

    public void checkWinners() {
        Set<Integer> winningNumbers = lottoNumberGeneratorFacade.getWinningNumbers();
        Set<String> winners = new HashSet<>();

        Iterable<Ticket> tickets = numberReceiverFacade.getAllTickets();
        tickets.forEach(t -> {
            if (t.getNumbers().equals(winningNumbers)) {
                winners.add(t.getHash());
            }
        });
        winnersRepository.saveWinners(winners);
    }
}
