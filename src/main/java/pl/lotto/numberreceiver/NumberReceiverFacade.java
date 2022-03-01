package pl.lotto.numberreceiver;

import java.util.Set;
import java.util.UUID;

public class NumberReceiverFacade {

    private final NumberValidator numberValidator;
    private final TicketRepository ticketRepository;

    NumberReceiverFacade(NumberValidator numbersValidator, TicketRepository ticketRepository) {
        this.numberValidator = numbersValidator;
        this.ticketRepository = ticketRepository;
    }

    public ResultMessage inputNumbers(Set<Integer> numbers) {
        if (numberValidator.numbersAreValid(numbers)) {
            String hash = UUID.randomUUID().toString();
            Ticket ticket = new Ticket(hash, numbers);
            //TreeSet<Integer> sortedNumbers = new TreeSet<>(numbers);
            ticketRepository.save(ticket);
            return new ResultMessage("Accepted", hash);
        } else {
            return new ResultMessage("Wrong amount of numbers or numbers out of range", "False");
        }
    }

    public Iterable<Ticket> getAllTickets() {
        return ticketRepository.findAll();
    }
}
