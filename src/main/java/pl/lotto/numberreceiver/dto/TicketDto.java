package pl.lotto.numberreceiver.dto;

import lombok.Builder;

import java.time.LocalDate;
import java.util.Set;

@Builder
public class TicketDto {

        private String hash;
        private Set<Integer> numbers;
        private LocalDate drawingDate;
}
