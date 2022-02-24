package pl.lotto.resultchecker;

import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import pl.lotto.lottonumbergenerator.dto.WinningNumbers;
import pl.lotto.numberreceiver.NumberReceiverFacade;
import pl.lotto.lottonumbergenerator.LottoNumberGenerator;

public class ResultCheckerFacade {

    private final WinnersRepository winnersRepository;
    private final NumberReceiverFacade numberReceiverFacade;
    private final LottoNumberGenerator lottoNumberGenerator;

    public ResultCheckerFacade(WinnersRepository winnersRepository,
                               NumberReceiverFacade numberReceiverFacade,
                               LottoNumberGenerator lottoNumberGenerator) {
        this.winnersRepository = winnersRepository;
        this.numberReceiverFacade = numberReceiverFacade;
        this.lottoNumberGenerator = lottoNumberGenerator;
    }

    public Set<String> getWinners() {
        return winnersRepository.getAllWinners();
    }

    public void checkWinners() {
        Map<String, Set<Integer>> usersNumbers = numberReceiverFacade.allNumbersFromUsers();
        WinningNumbers winningNumbersJSON = lottoNumberGenerator.generateNumbers();
        Set<Integer> winningNumbers = winningNumbersJSON.getWinningNumbers();
        Set<String> winners = new HashSet<>();
        usersNumbers.forEach((key, value) -> {
            if (value.equals(winningNumbers)) winners.add(key);
        });
        winnersRepository.saveWinners(winners);
    }
}
