package pl.lotto.numberreceiver.dto;

import lombok.Builder;

import java.time.LocalDate;
import java.util.List;
import java.util.Set;

@Builder
public class TicketsDto {

    List<TicketDto> list;
}
