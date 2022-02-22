package pl.lotto.resultchecker;

import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import pl.lotto.numberreceiver.NumberReceiverFacade;
import pl.lotto.proxy.GenerateNumbersProxy;

public class ResultCheckerFacade {

    private final WinnersRepository winnersRepository;
    private final NumberReceiverFacade numberReceiverFacade;
    private final GenerateNumbersProxy generateNumbersProxy;

    public ResultCheckerFacade(WinnersRepository winnersRepository,
                               NumberReceiverFacade numberReceiverFacade,
                               GenerateNumbersProxy generateNumbersProxy) {
        this.winnersRepository = winnersRepository;
        this.numberReceiverFacade = numberReceiverFacade;
        this.generateNumbersProxy = generateNumbersProxy;
    }

    public Set<String> getWinners() {
        return winnersRepository.getAllWinners();
    }

    public void checkWinners() {
        Map<String, Set<Integer>> usersNumbers = numberReceiverFacade.allNumbersFromUsers();
        Set<Integer> winningNumbers = generateNumbersProxy.generateNumbers();
        Set<String> winners = new HashSet<>();
        usersNumbers.forEach((key, value) -> {
            if (value.equals(winningNumbers)) winners.add(key);
        });
        winnersRepository.saveWinners(winners);
    }
}
