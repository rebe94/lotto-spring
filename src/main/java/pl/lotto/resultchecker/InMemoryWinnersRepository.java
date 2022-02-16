package pl.lotto.resultchecker;

import org.springframework.stereotype.Repository;

import java.util.HashSet;
import java.util.Set;

@Repository
public class InMemoryWinnersRepository implements WinnersRepository {

    private Set<String> winners = new HashSet<>();

    @Override
    public void saveWinners(Set<String> winners) {
        this.winners = winners;
    }

    @Override
    public Set<String> getAllWinners() {
        return winners;
    }
}
