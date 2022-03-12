package pl.lotto.resultannouncer.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import pl.lotto.resultchecker.ResultCheckerFacade;
import pl.lotto.resultchecker.dto.WinnerDto;

import java.util.List;

@Controller
class ResultAnnouncerController {

    private final ResultCheckerFacade resultCheckerFacade;

    @Autowired
    ResultAnnouncerController(ResultCheckerFacade resultCheckerFacade) {
        this.resultCheckerFacade = resultCheckerFacade;
    }

    @GetMapping("/announcer")
    String announcer() {
        return "announcer.html";
    }

    @PostMapping("/announcer")
    String announcer(
            @RequestParam String hashCode,
            Model page) {

        String messageResult = checkResult(hashCode);

        page.addAttribute("messageResult", messageResult);

        return "announcer.html";
    }

    private String checkResult(String hashCode) {
        String messageResult = "Loser";
        List<WinnerDto> winners = resultCheckerFacade.getAllWinners().getList();
        for (WinnerDto winner : winners) {
            if (winner.getHash().equals(hashCode)) {
                messageResult = "Winner";
                break;
            }
        }
        return messageResult;
    }
}
