package pl.lotto.lottonumbergenerator;

import lombok.Builder;
import lombok.Getter;
import pl.lotto.lottonumbergenerator.dto.WinningNumbersDto;

import java.util.Collections;
import java.util.Set;

@Builder
@Getter
class WinningNumberValidationResult {

    public static WinningNumbersDto valid(Set<Integer> winningNumbers) {
        return WinningNumbersDto.builder()
                .validationMessage(WinningNumbersDto.ValidationMessage.VALID)
                .winningNumbers(winningNumbers)
                .build();
    }

    public static WinningNumbersDto not_valid(Set<Integer> winningNumbers) {
        return WinningNumbersDto.builder()
                .validationMessage(WinningNumbersDto.ValidationMessage.NOT_VALID)
                .winningNumbers(winningNumbers)
                .build();
    }

    public static WinningNumbersDto failed() {
        return WinningNumbersDto.builder()
                .validationMessage(WinningNumbersDto.ValidationMessage.FAILED)
                .winningNumbers(Collections.emptySet())
                .build();
    }

    public static WinningNumbersDto numbers_not_exist() {
        return WinningNumbersDto.builder()
                .validationMessage(WinningNumbersDto.ValidationMessage.NOT_EXIST)
                .winningNumbers(Collections.emptySet())
                .build();
    }
}
