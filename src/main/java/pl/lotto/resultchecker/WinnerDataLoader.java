package pl.lotto.resultchecker;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;

import java.time.LocalDate;
import java.util.Arrays;
import java.util.List;
import java.util.Set;
import java.util.TreeSet;

class WinnerDataLoader implements ApplicationRunner {

    private final WinnerRepository winnerRepository;

    @Autowired
    public WinnerDataLoader(WinnerRepository winnerRepository) {
        this.winnerRepository = winnerRepository;
    }

    public void run(ApplicationArguments args) {
        Winner winner1 = new Winner("950e4df6-4243-41b1-bcfe-f130a789466f", new TreeSet<>(Set.of(1, 2, 3, 4, 5, 6)), LocalDate.of(2022, 3, 1));
        Winner winner2 = new Winner("370c485a-b17a-442a-9b99-e5e69da34039", new TreeSet<>(Set.of(1, 2, 3, 4, 5, 7)), LocalDate.of(2022, 3, 2));
        List<Winner> winners = Arrays.asList(winner1, winner2);
        winnerRepository.saveAll(winners);
    }
}
