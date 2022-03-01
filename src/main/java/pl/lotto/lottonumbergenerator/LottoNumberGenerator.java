package pl.lotto.lottonumbergenerator;

import pl.lotto.lottonumbergenerator.dto.WinningNumbersDto;

import java.time.LocalDate;

interface LottoNumberGenerator {

    WinningNumbersDto getWinningNumbers(LocalDate date);
}
