package pl.lotto.resultchecker;

import java.util.HashSet;
import java.util.Set;

class InMemoryWinnerRepository implements WinnerRepository {

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
