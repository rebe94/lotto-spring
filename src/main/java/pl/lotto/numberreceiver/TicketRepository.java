package pl.lotto.numberreceiver;

import org.springframework.data.mongodb.repository.MongoRepository;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

public interface TicketRepository extends MongoRepository<Ticket, String> {

    List<Ticket> findAll();

    Optional<Ticket> findByHash(String hash);

    List<Ticket> findAllByDrawDate(LocalDate drawDate);
}
