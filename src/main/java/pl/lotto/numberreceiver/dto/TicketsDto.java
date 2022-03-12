package pl.lotto.numberreceiver.dto;

import lombok.Builder;
import lombok.Data;

import java.time.LocalDate;
import java.util.List;
import java.util.Set;

@Builder
@Data
public class TicketsDto {

    private List<TicketDto> list;
}
