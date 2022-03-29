package pl.lotto.resultchecker;

import java.time.LocalDate;
import java.util.List;

import org.slf4j.Logger;
import org.springframework.scheduling.annotation.Scheduled;
import pl.lotto.lottonumbergenerator.LottoNumberGeneratorFacade;
import pl.lotto.lottonumbergenerator.dto.WinningNumbersDto;
import pl.lotto.numberreceiver.NumberReceiverFacade;
import pl.lotto.numberreceiver.dto.TicketsDto;
import pl.lotto.resultchecker.dto.WinnerDto;
import pl.lotto.resultchecker.dto.WinnersDto;

import static org.slf4j.LoggerFactory.getLogger;

public class ResultCheckerFacade {

    private static final Logger LOGGER = getLogger(ResultCheckerFacade.class.getName());

    private final WinnerRepository winnerRepository;
    private final NumberReceiverFacade numberReceiverFacade;
    private final LottoNumberGeneratorFacade lottoNumberGeneratorFacade;

    ResultCheckerFacade(WinnerRepository winnerRepository,
                        NumberReceiverFacade numberReceiverFacade,
                        LottoNumberGeneratorFacade lottoNumberGeneratorFacade) {
        this.winnerRepository = winnerRepository;
        this.numberReceiverFacade = numberReceiverFacade;
        this.lottoNumberGeneratorFacade = lottoNumberGeneratorFacade;
    }

    List<Winner> getWinnersList() {
        return winnerRepository.findAll();
    }

    public WinnersDto getAllWinners() {
        List<WinnerDto> winnersDto = winnerRepository.findAll()
                .stream()
                .map(winner -> WinnerDto.builder()
                        .hash(winner.getHash())
                        .numbers(winner.getNumbers())
                        .drawDate(winner.getDrawDate())
                        .build())
                .toList();

        return WinnersDto.builder()
                .list(winnersDto)
                .build();
    }

    @Scheduled(cron = "0 5 19 * * *")
    private void generateWinningNumbers() {
        LocalDate drawDate = LocalDate.now();
        checkWinnersAfterDraw(drawDate);
    }

    public void checkWinnersAfterDraw(LocalDate drawDate) {
        WinningNumbersDto winningNumbersDto = lottoNumberGeneratorFacade.getWinningNumbers(drawDate);
        if (!winningNumbersDto.getValidationMessage().equals(WinningNumbersDto.ValidationMessage.VALID)) {
            LOGGER.error("Couldn't check winners scheduled for " + drawDate);
            return;
        }

        TicketsDto ticketsDto = numberReceiverFacade.getAllTicketsByDrawDate(drawDate);
        List<Winner> winners = ticketsDto.getList().stream()
                .filter(ticketDto -> ticketDto.getNumbers().equals(winningNumbersDto.getWinningNumbers()))
                .map(ticketDto -> Winner.builder()
                        .hash(ticketDto.getHash())
                        .numbers(ticketDto.getNumbers())
                        .drawDate(ticketDto.getDrawDate())
                        .build())
                .toList();
        winnerRepository.saveAll(winners);
        LOGGER.info("Winners are correctly checked as scheduled for drawing date: " + drawDate);
    }
}
