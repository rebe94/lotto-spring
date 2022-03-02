package pl.lotto.numberreceiver;

import java.time.LocalDate;
import java.util.HashSet;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

class InMemoryTicketRepository implements TicketRepository {

    private final Set<Ticket> tickets = new HashSet<>();

    @Override
    public Ticket save(Ticket ticket) {
        tickets.add(ticket);
        return ticket; //TODO zwrócić rzeczywisty obiekt z pamięci/bazy
    }

    @Override
    public Set<Ticket> findAll() {
        return tickets;
    }

    @Override
    public Ticket findByHash(String hash) {
        Optional<Ticket> foundTicket = tickets.stream().filter(t -> t.getHash().equals(hash)).findFirst();
        return foundTicket.orElse(null);
    }

    @Override
    public Set<Ticket> findAllByDrawingDate(LocalDate drawingDate) {
        return tickets.stream().filter(t -> t.getDrawingDate().equals(drawingDate))
                .collect(Collectors.toSet());
    }
}
