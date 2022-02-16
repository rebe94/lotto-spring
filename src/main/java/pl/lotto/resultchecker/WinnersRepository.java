package pl.lotto.resultchecker;

import java.util.Set;

interface WinnersRepository {

    void saveWinners(Set<String> winners);
    Set<String> getAllWinners();
}
