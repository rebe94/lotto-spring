package pl.lotto.numberreceiver.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

import java.time.LocalDate;
import java.util.Set;

@Builder
@Data
@AllArgsConstructor
public class TicketDto {

    private String hash;
    private Set<Integer> numbers;
    private LocalDate drawDate;
}
