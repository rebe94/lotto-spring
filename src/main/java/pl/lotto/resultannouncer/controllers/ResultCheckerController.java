package pl.lotto.resultannouncer.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import pl.lotto.numberreceiver.NumberReceiverFacade;
import pl.lotto.numberreceiver.ResultMessage;
import pl.lotto.resultchecker.ResultCheckerFacade;

import java.util.Set;
import java.util.TreeSet;

import static pl.lotto.configuration.GameConfiguration.AMOUNT_OF_NUMBERS;
import static pl.lotto.configuration.GameConfiguration.HIGHEST_NUMBER;
import static pl.lotto.configuration.GameConfiguration.LOWEST_NUMBER;

@Controller
public class ResultCheckerController {

    private ResultCheckerFacade resultCheckerFacade;

    @Autowired
    public ResultCheckerController(ResultCheckerFacade resultCheckerFacade) {
        this.resultCheckerFacade = resultCheckerFacade;
    }

    @GetMapping("/checker")
    public String check() {
        return "checker.html";
    }

    @PostMapping("/checker")
    public String check(
            @RequestParam String hashCode,
            Model page) {

        String messageResult = checkResult(hashCode);

        page.addAttribute("messageResult", messageResult);

        return "checker.html";
    }

    private String checkResult(String hashCode) {
        String messageResult = "Loser";
        resultCheckerFacade.checkWinners();
        Set<String> winners = resultCheckerFacade.getWinners();
        for (String winner : winners) {
            if (winner.equals(hashCode)) {
                messageResult = "Winner";
                break;
            }
        }
        return messageResult;
    }
}
