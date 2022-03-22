package pl.lotto.resultannouncer.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import pl.lotto.configuration.GameConfiguration;
import pl.lotto.resultannouncer.ResultAnnouncerFacade;
import pl.lotto.resultchecker.ResultCheckerFacade;
import pl.lotto.resultchecker.dto.WinnerDto;

import java.util.List;

import static pl.lotto.configuration.GameConfiguration.AMOUNT_OF_NUMBERS;
import static pl.lotto.configuration.GameConfiguration.DRAW_TIME;
import static pl.lotto.configuration.GameConfiguration.HIGHEST_NUMBER;
import static pl.lotto.configuration.GameConfiguration.LOWEST_NUMBER;
import static pl.lotto.configuration.GameConfiguration.TICKET_RECEIVER_CLOSING_TIME;

@Controller
class ResultAnnouncerController {

    private final ResultAnnouncerFacade resultAnnouncerFacade;

    @Autowired
    ResultAnnouncerController(ResultAnnouncerFacade resultAnnouncerFacade) {
        this.resultAnnouncerFacade = resultAnnouncerFacade;
    }

    @GetMapping("/announcer")
    String announcer(
            @RequestParam(required = false) String hashCode,
            Model page) {
        if (hashCode == null) {
            return "announcer.html";
        }
        String messageResult = resultAnnouncerFacade.checkResult(hashCode);
        page.addAttribute("messageResult", messageResult);
        return "announcer.html";
    }
}
