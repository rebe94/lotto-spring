package pl.lotto.numberreceiver;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.LocalDate;
import java.util.Set;

@Getter
@Setter
@Document(collection = "tickets")
public class Ticket {

    @Id
    private String id;
    private String hash;
    private Set<Integer> numbers;
    private LocalDate drawDate;

    @Builder
    public Ticket(String hash, Set<Integer> numbers, LocalDate drawDate) {
        this.hash = hash;
        this.numbers = numbers;
        this.drawDate = drawDate;
    }
}
