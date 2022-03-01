package pl.lotto.numberreceiver;

import lombok.Getter;
import lombok.Setter;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.Set;

@Getter
@Setter
@Document("tickets")
public class Ticket {

  @Id
  private String id;

  private String hash;
  private Set<Integer> numbers;

  public Ticket(String hash, Set<Integer> numbers) {
    this.hash = hash;
    this.numbers = numbers;
  }
}
