package pl.lotto.numberreceiver;

import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.List;

public interface DatabaseTicketRepository extends MongoRepository<Ticket, String>, TicketRepository {

  Ticket save(Ticket ticket);

  List<Ticket> findAll();
}
