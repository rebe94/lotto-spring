package pl.lotto.resultchecker.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

import java.time.LocalDate;
import java.util.Set;

@Builder
@Data
@AllArgsConstructor
public class WinnerDto {

        private String hash;
        private Set<Integer> numbers;
        private LocalDate drawingDate;
}
