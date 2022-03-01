package pl.lotto.numberreceiver;

import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

class InMemoryTicketRepository implements TicketRepository {

    private final Set<Ticket> numbers = new HashSet<>();

    @Override
    public Ticket save(Ticket ticket) {
        numbers.add(ticket);
        return ticket; //TODO zwrócić rzeczywisty obiekt z pamięci/bazy
    }
    @Override
    public List<Ticket> findAll() {
        return null;
    }
}
