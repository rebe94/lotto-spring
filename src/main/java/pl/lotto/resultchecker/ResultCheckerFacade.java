package pl.lotto.resultchecker;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import pl.lotto.lottonumbergenerator.LottoNumberGeneratorFacade;
import pl.lotto.numberreceiver.NumberReceiverFacade;

import java.util.HashSet;
import java.util.Map;
import java.util.Set;

@Service
public class ResultCheckerFacade {

    private final WinnersRepository winnersRepository;
    private final NumberReceiverFacade numberReceiverFacade;
    private final LottoNumberGeneratorFacade lottoNumberGeneratorFacade;

    @Autowired
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
        System.out.println("Wewnątrz implementacji metody checkWinners(): " + lottoNumberGeneratorFacade.winningNumbers());
        Set<String> winners = new HashSet<>();
        usersNumbers.forEach((key, value) -> {
            if (value.equals(winningNumbers)) winners.add(key);
        });
        winnersRepository.saveWinners(winners);
    }
}
