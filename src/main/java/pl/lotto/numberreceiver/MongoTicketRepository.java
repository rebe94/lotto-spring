package pl.lotto.numberreceiver;

import org.springframework.data.mongodb.repository.MongoRepository;

public interface MongoTicketRepository
        extends TicketRepository, MongoRepository<Ticket, String> {
}
