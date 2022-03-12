package pl.lotto.numberreceiver;

import pl.lotto.configuration.GameConfiguration;
import pl.lotto.numberreceiver.dto.TicketDto;
import pl.lotto.numberreceiver.dto.TicketsDto;

import java.time.LocalDate;
import java.util.Collections;
import java.util.List;
import java.util.Set;
import java.util.TreeSet;
import java.util.UUID;
import java.util.stream.Collectors;

public class NumberReceiverFacade {

    private final NumberValidator numberValidator;
    private final TicketRepository ticketRepository;

    public NumberReceiverFacade(NumberValidator numbersValidator, TicketRepository ticketRepository) {
        this.numberValidator = numbersValidator;
        this.ticketRepository = ticketRepository;
    }

    public ResultMessage inputNumbers(Set<Integer> numbers) {
        if (numberValidator.numbersAreValid(numbers)) {
            String hash = UUID.randomUUID().toString();
            Ticket ticket = Ticket.builder()
                    .hash(hash)
                    .numbers(new TreeSet<>(numbers))
                    .drawingDate(GameConfiguration.nextDrawingDate())
                    .build();
            ticketRepository.save(ticket);
            Ticket addedTicket = ticketRepository.findByHash(hash);
            return new ResultMessage("Accepted", addedTicket.getHash(), addedTicket.getDrawingDate().toString()); //TODO czy nie powinno tu być @Transactional?
        } else {
            return new ResultMessage("Wrong amount of numbers or numbers out of range", "False", "False");
        }
    }

    public TicketsDto getAllTicketsByDrawingDate(LocalDate drawingDate) {
        List<TicketDto> ticketsDto = ticketRepository.findAllTicketsByDrawingDate(drawingDate)
                .stream()
                .map(ticket -> TicketDto.builder()
                        .hash(ticket.getHash())
                        .numbers(ticket.getNumbers())
                        .drawingDate(ticket.getDrawingDate())
                        .build())
                .toList();

        return TicketsDto.builder()
                .list(ticketsDto)
                .build();
    }
}
