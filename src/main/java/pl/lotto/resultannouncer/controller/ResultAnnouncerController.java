package pl.lotto.resultannouncer.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import pl.lotto.resultannouncer.ResultAnnouncerFacade;

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
