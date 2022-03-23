package pl.lotto.numberreceiver.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.ui.Model;
import pl.lotto.configuration.GameConfiguration;
import pl.lotto.numberreceiver.NumberReceiverFacade;
import pl.lotto.numberreceiver.dto.ResultMessageDto;

import java.time.format.DateTimeFormatter;
import java.util.Collections;
import java.util.Set;
import java.util.TreeSet;

import static pl.lotto.configuration.GameConfiguration.AMOUNT_OF_NUMBERS;
import static pl.lotto.configuration.GameConfiguration.DRAW_TIME;
import static pl.lotto.configuration.GameConfiguration.HIGHEST_NUMBER;
import static pl.lotto.configuration.GameConfiguration.LOWEST_NUMBER;
import static pl.lotto.configuration.GameConfiguration.TICKET_RECEIVER_CLOSING_TIME;

@Controller
class NumberReceiverController {

    private final NumberReceiverFacade numberReceiverFacade;
    private final DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern("dd-MM-yyyy");

    @Autowired
    NumberReceiverController(NumberReceiverFacade numberReceiverFacade) {
        this.numberReceiverFacade = numberReceiverFacade;
    }

    @GetMapping("/receiver")
    String receiver(Model page) {
        initialize(page);
        return "receiver.html";
    }

    @PostMapping("/receiver")
    String receiver(
            @RequestParam String input,
            Model page) {
        initialize(page);
        Set<Integer> setOfNumbers = parseToSetOfNumbers(input);
        ResultMessageDto resultMessageDto = numberReceiverFacade.inputNumbers(setOfNumbers);
        page.addAttribute("messageForUser", resultMessageDto.getMessage());
        page.addAttribute("generatedHash", resultMessageDto.getHash());
        page.addAttribute("drawDate", resultMessageDto.getDrawDate());
        return "receiver.html";
    }

    private void initialize(Model page) {
        page.addAttribute("amountOfNumbers", AMOUNT_OF_NUMBERS);
        page.addAttribute("lowestNumber", LOWEST_NUMBER);
        page.addAttribute("highestNumber", HIGHEST_NUMBER);
        page.addAttribute("dateTimeOfNextDraw", GameConfiguration.nextDrawDate().format(dateFormatter).toString());
        page.addAttribute("drawTime", DRAW_TIME.toString());
        page.addAttribute("ticketReceiverClosingTime", TICKET_RECEIVER_CLOSING_TIME.toString());
    }

    Set<Integer> parseToSetOfNumbers(String input) {
        Set<Integer> setOfNumbers = new TreeSet<>();
        String[] splitted = input.trim().split(" ");
        try {
            for (String s : splitted) {
                setOfNumbers.add(Integer.parseInt(s));
            }
        } catch (Exception e) {
            e.printStackTrace();
            return Collections.emptySet();
        }
        return setOfNumbers;
    }
}
