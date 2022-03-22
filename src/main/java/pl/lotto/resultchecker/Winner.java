package pl.lotto.resultchecker;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.LocalDate;
import java.util.Set;

@Getter
@Setter
@Document(collection = "winners")
class Winner {

    @Id
    private String id;
    private String hash;
    private Set<Integer> numbers;
    private LocalDate drawDate;

    @Builder
    public Winner(String hash, Set<Integer> numbers, LocalDate drawDate) {
        this.hash = hash;
        this.numbers = numbers;
        this.drawDate = drawDate;
    }
}
