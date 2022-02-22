package pl.lotto.resultchecker;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.TreeSet;

import org.thymeleaf.expression.Arrays;
import pl.lotto.lottonumbergenerator.LottoNumberGeneratorFacade;
import pl.lotto.numberreceiver.NumberReceiverFacade;
import pl.lotto.proxy.GenerateNumbersProxy;

public class ResultCheckerFacade {

    private final WinnersRepository winnersRepository;
    private final NumberReceiverFacade numberReceiverFacade;
    private final LottoNumberGeneratorFacade lottoNumberGeneratorFacade;
    private final GenerateNumbersProxy generateNumbersProxy;

    public ResultCheckerFacade(WinnersRepository winnersRepository,
                               NumberReceiverFacade numberReceiverFacade,
                               LottoNumberGeneratorFacade lottoNumberGeneratorFacade, GenerateNumbersProxy generateNumbersProxy) {
        this.winnersRepository = winnersRepository;
        this.numberReceiverFacade = numberReceiverFacade;
        this.lottoNumberGeneratorFacade = lottoNumberGeneratorFacade;
        this.generateNumbersProxy = generateNumbersProxy;
    }

    public Set<String> getWinners() {
        return winnersRepository.getAllWinners();
    }

    public void checkWinners() {
        Map<String, Set<Integer>> usersNumbers = numberReceiverFacade.allNumbersFromUsers();
        //Set<Integer> winningNumbers = lottoNumberGeneratorFacade.winningNumbers();
        Set<Integer> winningNumbers = generateNumbersProxy.generateNumbers();
        Set<String> winners = new HashSet<>();
        usersNumbers.forEach((key, value) -> {
            if (value.equals(winningNumbers)) winners.add(key);
        });
        winnersRepository.saveWinners(winners);
    }
}
