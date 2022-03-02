package pl.lotto.numberreceiver;

import java.time.LocalDate;
import java.util.Collection;
import java.util.List;

interface TicketRepository {

    Ticket save(Ticket ticket);

    Iterable<Ticket> findAll();

    Ticket findByHash(String hash);

    Collection<Ticket> findAllByDrawingDate(LocalDate drawingDate);
}
