package pl.lotto.numberreceiver;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

class InMemoryTicketRepository implements TicketRepository {

    private final Map<String, Set<Integer>> numbers = new HashMap<>();

    @Override
    public Ticket save(Ticket ticket) {
        //numbers.put(hash, userNumbers);
        return null;
    }
    @Override
    public List<Ticket> findAll() {
        return null;
    }
}
