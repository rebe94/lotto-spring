package pl.lotto.numberreceiver;

import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.List;

public interface DatabaseTicketRepository extends TicketRepository//, MongoRepository<Ticket, String>
{
  Ticket save(Ticket ticket);

  List<Ticket> findAll();
}
