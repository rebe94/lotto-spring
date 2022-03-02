package pl.lotto.numberreceiver;

import pl.lotto.configuration.GameConfiguration;
import pl.lotto.numberreceiver.dto.TicketDto;
import pl.lotto.numberreceiver.dto.TicketsDto;

import java.time.LocalDate;
import java.util.List;
import java.util.Set;
import java.util.UUID;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class NumberReceiverFacade {

    private final NumberValidator numberValidator;
    private final TicketRepository ticketRepository;

    public NumberReceiverFacade(NumberValidator numbersValidator, TicketRepository ticketRepository) {
        this.numberValidator = numbersValidator;
        this.ticketRepository = ticketRepository;Ń
    }

    public ResultMessage inputNumbers(Set<Integer> numbers) {
        if (numberValidator.numbersAreValid(numbers)) {
            String hash = UUID.randomUUID().toString();
            Ticket ticket = new Ticket(hash, numbers, GameConfiguration.nextDrawingDate());
            ticketRepository.save(ticket);
            Ticket addedTicket = ticketRepository.findByHash(hash);
            return new ResultMessage("Accepted", addedTicket.getHash(), addedTicket.getDrawingDate().toString()); //TODO czy nie powinno tu być @Transactional?
        } else {
            return new ResultMessage("Wrong amount of numbers or numbers out of range", "False", "False");
        }
    }

    public TicketsDto getAllTicketsByDrawingDate(LocalDate drawingDate) {
        List<TicketDto> ticketsDto = ticketRepository.findAllByDrawingDate(drawingDate)
                .stream()
                .map(ticket -> TicketDto.builder()
                        .drawingDate(ticket.getDrawingDate())
                        .hash(ticket.getHash())
                        .build())
                .collect(Collectors.toList());

        return TicketsDto.builder()
                .list(ticketsDto)
                .build();
    }
}
