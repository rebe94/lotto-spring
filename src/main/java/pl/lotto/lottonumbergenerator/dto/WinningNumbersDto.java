package pl.lotto.lottonumbergenerator.dto;

import lombok.Builder;
import lombok.Getter;

import java.util.Set;

@Builder
@Getter
public class WinningNumbersDto {

    private Set<Integer> winningNumbers;
    private ValidationMessage validationMessage;

    public enum ValidationMessage {
        VALID,
        NOT_VALID,
        FAILED,
        NOT_EXIST
    }
}


