package pl.lotto.numberreceiver;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Map;
import java.util.Set;
import java.util.TreeSet;
import java.util.UUID;

@Service
public class NumberReceiverFacade {

    private final NumberValidator numberValidator;
    private final NumberRepository numberRepository;

    @Autowired
    public NumberReceiverFacade(NumberValidator numbersValidator, NumberRepository numberRepository) {
        this.numberValidator = numbersValidator;
        this.numberRepository = numberRepository;
    }

    public ResultMessage inputNumbers(Set<Integer> numbers) {
        if (numberValidator.numbersAreValid(numbers)) {
            String hash = UUID.randomUUID().toString();
            TreeSet<Integer> sortedNumbers = new TreeSet<>(numbers);
            numberRepository.save(hash, sortedNumbers);
            return new ResultMessage("Accepted", hash);
        } else {
            return new ResultMessage("Not accepted", "False");
        }
    }

    public Map<String, Set<Integer>> allNumbersFromUsers() {
        return numberRepository.getAllNumbers();
    }
}