package pl.lotto.lottonumbergenerator;

import pl.lotto.lottonumbergenerator.dto.WinningNumbersDto;

import java.util.Set;
import java.util.TreeSet;

public class LottoNumberGeneratorFacade {

    private final LottoNumberGenerator lottoNumberGenerator;

    LottoNumberGeneratorFacade(LottoNumberGenerator lottoNumberGenerator) {
        this.lottoNumberGenerator = lottoNumberGenerator;
    }

    public Set<Integer> getWinningNumbers() {
        WinningNumbersDto winningNumbersDto = lottoNumberGenerator.getWinningNumbers();
        return new TreeSet<>(winningNumbersDto.getWinningNumbers());
    }
}
