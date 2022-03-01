package pl.lotto.numberreceiver;

import java.util.List;

interface TicketRepository {

    Ticket save(Ticket ticket);

    List<Ticket> findAll();
}
