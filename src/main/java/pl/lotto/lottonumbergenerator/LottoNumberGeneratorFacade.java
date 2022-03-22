package pl.lotto.lottonumbergenerator;

import pl.lotto.lottonumbergenerator.dto.WinningNumbersDto;

import java.time.LocalDate;

public class LottoNumberGeneratorFacade {

    private final LottoNumberGenerator lottoNumberGenerator;

    LottoNumberGeneratorFacade(LottoNumberGenerator lottoNumberGenerator) {
        this.lottoNumberGenerator = lottoNumberGenerator;
    }

    public WinningNumbersDto getWinningNumbers(LocalDate drawingDate) {
        return lottoNumberGenerator.getWinningNumbersRequest(drawingDate);
    }
}
