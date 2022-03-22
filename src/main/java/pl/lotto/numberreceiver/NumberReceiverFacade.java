package pl.lotto.numberreceiver;

import pl.lotto.configuration.GameConfiguration;
import pl.lotto.numberreceiver.dto.ResultMessageDto;
import pl.lotto.numberreceiver.dto.TicketDto;
import pl.lotto.numberreceiver.dto.TicketsDto;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.TreeSet;
import java.util.UUID;

import static pl.lotto.numberreceiver.NumberReceiverValidationResult.accepted;
import static pl.lotto.numberreceiver.NumberReceiverValidationResult.failed;

public class NumberReceiverFacade {

    private final NumberValidator numberValidator;
    private final TicketRepository ticketRepository;

    public NumberReceiverFacade(NumberValidator numbersValidator, TicketRepository ticketRepository) {
        this.numberValidator = numbersValidator;
        this.ticketRepository = ticketRepository;
    }

    public ResultMessageDto inputNumbers(Set<Integer> numbers) {
        if (numberValidator.isNumberValid(numbers)) {
            String hash = UUID.randomUUID().toString();
            Ticket generatedTicket = Ticket.builder()
                    .hash(hash)
                    .numbers(new TreeSet<>(numbers))
                    .drawDate(GameConfiguration.nextDrawDate())
                    .build();
            ticketRepository.save(generatedTicket);
            Optional<Ticket> addedTicket = ticketRepository.findByHash(hash);
            if (addedTicket.isPresent()) {
                return accepted(addedTicket.get()); //TODO czy nie powinno być tutaj @Transactional?
            }
        }
        return failed();
    }

    public Optional<Ticket> findByHash(String hash) {
        return ticketRepository.findByHash(hash);
    }

    public TicketsDto getAllTicketsByDrawDate(LocalDate drawDate) {
        List<TicketDto> ticketsDto = ticketRepository.findAllByDrawDate(drawDate)
                .stream()
                .map(ticket -> TicketDto.builder()
                        .hash(ticket.getHash())
                        .numbers(ticket.getNumbers())
                        .drawDate(ticket.getDrawDate())
                        .build())
                .toList();

        return TicketsDto.builder()
                .list(ticketsDto)
                .build();
    }
}
