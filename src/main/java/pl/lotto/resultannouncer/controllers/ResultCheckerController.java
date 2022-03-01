package pl.lotto.resultannouncer.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import pl.lotto.resultchecker.ResultCheckerFacade;

import java.util.Set;

@Controller
class ResultCheckerController {

    private ResultCheckerFacade resultCheckerFacade;

    @Autowired
    ResultCheckerController(ResultCheckerFacade resultCheckerFacade) {
        this.resultCheckerFacade = resultCheckerFacade;
    }

    @GetMapping("/checker")
    String checker() {
        return "checker.html";
    }

    @PostMapping("/checker")
    String checker(
            @RequestParam String hashCode,
            Model page) {

        String messageResult = checkResult(hashCode);

        page.addAttribute("messageResult", messageResult);

        return "checker.html";
    }

    private String checkResult(String hashCode) {
        String messageResult = "Loser";
        resultCheckerFacade.checkWinnersAfterDraw();
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
