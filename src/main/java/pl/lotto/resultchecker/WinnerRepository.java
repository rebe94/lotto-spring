package pl.lotto.resultchecker;

import pl.lotto.numberreceiver.Ticket;

import java.util.Set;

interface WinnerRepository {

    Winner save(Winner winner);

    void saveWinners(Set<String> winners);
    Set<String> getAllWinners();
}
