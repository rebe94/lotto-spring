package pl.lotto.resultchecker;

import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import pl.lotto.lottonumbergenerator.LottoNumberGeneratorFacade;
import pl.lotto.numberreceiver.NumberReceiverFacade;

public class ResultCheckerFacade {

    private final WinnersRepository winnersRepository;
    private final NumberReceiverFacade numberReceiverFacade;
    private final LottoNumberGeneratorFacade lottoNumberGeneratorFacade;

    public ResultCheckerFacade(WinnersRepository winnersRepository,
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
        Map<String, Set<Integer>> usersNumbers = numberReceiverFacade.allNumbersFromUsers();
        Set<Integer> winningNumbers = lottoNumberGeneratorFacade.winningNumbers();
        Set<String> winners = new HashSet<>();
        usersNumbers.forEach((key, value) -> {
            if (value.equals(winningNumbers)) winners.add(key);
        });
        winnersRepository.saveWinners(winners);
    }
}
