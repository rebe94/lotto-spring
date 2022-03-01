package pl.lotto.lottonumbergenerator.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.Set;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class WinningNumbersDto {

    private Set<Integer> winningNumbers;
}
