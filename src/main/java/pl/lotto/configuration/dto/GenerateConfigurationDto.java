package pl.lotto.configuration.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class GenerateConfigurationDto {

    private Integer amountOfNumbers, lowestNumber, highestNumber;
}
