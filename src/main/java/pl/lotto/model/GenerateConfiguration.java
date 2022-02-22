package pl.lotto.model;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class GenerateConfiguration {

    private Integer amountOfNumbers, lowestNumber, highestNumber;

    public static GenerateConfiguration of(Integer amountOfNumbers, Integer lowestNumber, Integer highestNumber) {
        GenerateConfiguration generateConfiguration = new GenerateConfiguration();
        generateConfiguration.setAmountOfNumbers(amountOfNumbers);
        generateConfiguration.setLowestNumber(lowestNumber);
        generateConfiguration.setHighestNumber(highestNumber);
        return generateConfiguration;
    }

    @Override
    public String toString() {
        return "GenerateConfiguration{" +
                "amountOfNumbers=" + amountOfNumbers +
                ", lowestNumber=" + lowestNumber +
                ", highestNumber=" + highestNumber +
                '}';
    }
}
