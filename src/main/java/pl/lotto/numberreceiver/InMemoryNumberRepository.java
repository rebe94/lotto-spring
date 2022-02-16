package pl.lotto.numberreceiver;

import org.springframework.stereotype.Repository;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

@Repository
public class InMemoryNumberRepository implements NumberRepository {

    private final Map<String, Set<Integer>> numbers = new HashMap<>();

    @Override
    public void save(String hash, Set<Integer> userNumbers) {
        numbers.put(hash, userNumbers);
    }

    @Override
    public Map<String, Set<Integer>> getAllNumbers() {
        return numbers;
    }
}
