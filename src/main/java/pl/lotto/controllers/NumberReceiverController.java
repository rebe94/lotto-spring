package pl.lotto.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.ui.Model;
import pl.lotto.numberreceiver.NumberReceiverFacade;
import pl.lotto.numberreceiver.ResultMessage;

import java.util.Set;
import java.util.TreeSet;

import static pl.lotto.configuration.GameConfiguration.AMOUNT_OF_NUMBERS;
import static pl.lotto.configuration.GameConfiguration.HIGHEST_NUMBER;
import static pl.lotto.configuration.GameConfiguration.LOWEST_NUMBER;

@Controller
public class NumberReceiverController {

    @Autowired
    private NumberReceiverFacade numberReceiverFacade;

    @GetMapping("/receive")
    public String receive(Model page) {
        init(page);
        return "receive.html";
    }

    @PostMapping("/receive")
    public String receive(
            @RequestParam String numbers,
            Model page) {

        init(page);
        String messageForUser = "Error due to not integers or space key mistake";
        String generatedHash = "False";

        Set<Integer> setOfNumbers = checkIntegerNumbers(numbers);
        if (setOfNumbers != null) {
            ResultMessage resultMessage = numberReceiverFacade.inputNumbers(setOfNumbers);
            messageForUser = resultMessage.getMessage();
            generatedHash = resultMessage.getHash();
        }

        page.addAttribute("messageForUser", messageForUser);
        page.addAttribute("generatedHash", generatedHash);

        return "receive.html";
    }

    private void init(Model page) {
        page.addAttribute("amountOfNumbers", AMOUNT_OF_NUMBERS);
        page.addAttribute("lowestNumber", LOWEST_NUMBER);
        page.addAttribute("highestNumber", HIGHEST_NUMBER);
    }

    private Set<Integer> checkIntegerNumbers(String numbers) {
        Set<Integer> setOfNumbers = new TreeSet<>();
        String[] splited = numbers.trim().split(" ");
        try {
            for (String s : splited) {
                setOfNumbers.add(Integer.parseInt(s));
            }
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
        return setOfNumbers;
    }
}
