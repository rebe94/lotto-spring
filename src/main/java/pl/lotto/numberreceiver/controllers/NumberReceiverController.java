package pl.lotto.numberreceiver.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.ui.Model;
import pl.lotto.configuration.GameConfiguration;
import pl.lotto.numberreceiver.NumberReceiverFacade;
import pl.lotto.numberreceiver.ResultMessage;

import java.util.Set;
import java.util.TreeSet;

import static pl.lotto.configuration.GameConfiguration.AMOUNT_OF_NUMBERS;
import static pl.lotto.configuration.GameConfiguration.DRAWING_TIME;
import static pl.lotto.configuration.GameConfiguration.HIGHEST_NUMBER;
import static pl.lotto.configuration.GameConfiguration.LOWEST_NUMBER;

@Controller
class NumberReceiverController {

    private NumberReceiverFacade numberReceiverFacade;

    @Autowired
    NumberReceiverController(NumberReceiverFacade numberReceiverFacade) {
        this.numberReceiverFacade = numberReceiverFacade;
    }

    @GetMapping("/receiver")
    String receiver(Model page) {
        init(page);
        return "receiver.html";
    }

    @PostMapping("/receiver")
    String receiver(
            @RequestParam String numbers,
            Model page) {

        init(page);
        String messageForUser = "Error due to not integers or space key mistake";
        String generatedHash = "False";
        String drawingDate = "False";

        Set<Integer> setOfNumbers = checkIntegerNumbers(numbers);
        if (setOfNumbers != null) {
            ResultMessage resultMessage = numberReceiverFacade.inputNumbers(setOfNumbers);
            messageForUser = resultMessage.getMessage();
            generatedHash = resultMessage.getHash();
            drawingDate = resultMessage.getDrawingDate();
        }

        page.addAttribute("messageForUser", messageForUser);
        page.addAttribute("generatedHash", generatedHash);
        page.addAttribute("drawingDate", drawingDate);

        return "receiver.html";
    }

    private void init(Model page) {
        page.addAttribute("amountOfNumbers", AMOUNT_OF_NUMBERS);
        page.addAttribute("lowestNumber", LOWEST_NUMBER);
        page.addAttribute("highestNumber", HIGHEST_NUMBER);
        page.addAttribute("dateTimeOfNextDraw", GameConfiguration.nextDrawingDate().toString());
        page.addAttribute("timeOfDrawing", DRAWING_TIME.toString());
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
