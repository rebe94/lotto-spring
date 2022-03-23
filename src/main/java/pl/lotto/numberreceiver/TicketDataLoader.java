package pl.lotto.numberreceiver;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;

import java.time.LocalDate;
import java.util.Arrays;
import java.util.List;
import java.util.Set;
import java.util.TreeSet;

class TicketDataLoader implements ApplicationRunner {

    private TicketRepository ticketRepository;

    @Autowired
    public TicketDataLoader(TicketRepository ticketRepository) {
        this.ticketRepository = ticketRepository;
    }

    public void run(ApplicationArguments args) {
        Ticket win_ticket1 = new Ticket("950e4df6-4243-41b1-bcfe-f130a789466f", new TreeSet<>(Set.of(1, 2, 3, 4, 5, 6)), LocalDate.of(2022, 3, 1));
        Ticket win_ticket2 = new Ticket("370c485a-b17a-442a-9b99-e5e69da34039", new TreeSet<>(Set.of(1, 2, 3, 4, 5, 7)), LocalDate.of(2022, 3, 2));
        Ticket lose_ticket1 = new Ticket("9853f6f2-0a06-4743-8056-86e7235a4497", new TreeSet<>(Set.of(1, 2, 3, 4, 5, 8)), LocalDate.of(2022, 3, 1));
        Ticket lose_ticket2 = new Ticket("cb22e5c5-feb3-4bc8-be88-5a01fb48f6f7", new TreeSet<>(Set.of(1, 2, 3, 4, 5, 9)), LocalDate.of(2022, 3, 2));
        List<Ticket> tickets = Arrays.asList(win_ticket1, win_ticket2, lose_ticket1, lose_ticket2);
        ticketRepository.saveAll(tickets);
    }
}
