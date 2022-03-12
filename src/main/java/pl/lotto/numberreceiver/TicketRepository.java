package pl.lotto.numberreceiver;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.Collection;
import java.util.List;

interface TicketRepository extends MongoRepository<Ticket, String> {

    Ticket save(Ticket ticket);

    List<Ticket> findAll();

    Ticket findByHash(String hash);

    List<Ticket> findAllTicketsByDrawingDate(LocalDate drawingDate);
}
