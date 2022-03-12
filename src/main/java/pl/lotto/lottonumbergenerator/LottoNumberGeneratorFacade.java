package pl.lotto.lottonumbergenerator;

import org.springframework.beans.factory.annotation.Qualifier;
import pl.lotto.lottonumbergenerator.dto.WinningNumbersDto;

import java.time.LocalDate;
import java.util.Set;
import java.util.TreeSet;

public class LottoNumberGeneratorFacade {

    private final LottoNumberGenerator lottoNumberGenerator;

    LottoNumberGeneratorFacade(LottoNumberGenerator lottoNumberGenerator) {
        this.lottoNumberGenerator = lottoNumberGenerator;
    }

    public Set<Integer> getWinningNumbers(LocalDate date) {
        WinningNumbersDto winningNumbersDto = lottoNumberGenerator.getWinningNumbers(date);
        return new TreeSet<>(winningNumbersDto.getWinningNumbers());
    }
}
