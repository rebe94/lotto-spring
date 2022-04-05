package pl.lotto.resultannouncer.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import pl.lotto.lottonumbergenerator.LottoNumberGeneratorFacade;
import pl.lotto.lottonumbergenerator.dto.WinningNumbersDto;
import pl.lotto.numberreceiver.NumberReceiverFacade;
import pl.lotto.numberreceiver.Ticket;
import pl.lotto.resultannouncer.ResultAnnouncerFacade;

import java.util.TreeSet;

import static pl.lotto.lottonumbergenerator.dto.WinningNumbersDto.ValidationMessage.VALID;
import static pl.lotto.resultannouncer.ResultAnnouncerMessageProvider.lose_message;
import static pl.lotto.resultannouncer.ResultAnnouncerMessageProvider.win_message;

@Controller
class ResultAnnouncerController {

    private final ResultAnnouncerFacade resultAnnouncerFacade;
    private final LottoNumberGeneratorFacade lottoNumberGeneratorFacade;
    private final NumberReceiverFacade numberReceiverFacade;

    private String messageResult, userNumbers, winningNumbers;

    @Autowired
    ResultAnnouncerController(ResultAnnouncerFacade resultAnnouncerFacade, LottoNumberGeneratorFacade lottoNumberGeneratorFacade, NumberReceiverFacade numberReceiverFacade) {
        this.resultAnnouncerFacade = resultAnnouncerFacade;
        this.lottoNumberGeneratorFacade = lottoNumberGeneratorFacade;
        this.numberReceiverFacade = numberReceiverFacade;
    }

    @GetMapping("/announcer")
    String announcer(
            @RequestParam(required = false) String hashCode,
            Model page) {
        if (hashCode == null) {
            return "announcer.html";
        }
        messageResult = resultAnnouncerFacade.checkResult(hashCode);

        winningNumbers = "";
        userNumbers = "";
        if (!messageResult.equals(win_message()) && !messageResult.equals(lose_message())) {
            loadValues(page);
            return "announcer.html";
        }

        userNumbers = "Ticket not found";
        winningNumbers = "Ticket not found";
        if (numberReceiverFacade.findByHash(hashCode).isEmpty()) {
            loadValues(page);
            return "announcer.html";
        }

        Ticket ticket = numberReceiverFacade.findByHash(hashCode).get();
        userNumbers = ticket.getNumbers().toString();
        WinningNumbersDto winningNumbersDto = lottoNumberGeneratorFacade.getWinningNumbers(ticket.getDrawDate());
        if (!winningNumbersDto.getValidationMessage().equals(VALID)) {
            winningNumbers = "Error";
            loadValues(page);
            return "announcer.html";
        }

        winningNumbers = new TreeSet<>(winningNumbersDto.getWinningNumbers()).toString();
        loadValues(page);
        return "announcer.html";
    }

    private void loadValues(Model page) {
        page.addAttribute("messageResult", messageResult);
        page.addAttribute("winningNumbers", winningNumbers);
        page.addAttribute("userNumbers", userNumbers);
    }
}
