package pl.lotto.lottonumbergenerator;

import pl.lotto.lottonumbergenerator.dto.WinningNumbersDto;

import java.time.LocalDate;

public interface LottoNumberGenerator {

    WinningNumbersDto getWinningNumbers(LocalDate date);
}
