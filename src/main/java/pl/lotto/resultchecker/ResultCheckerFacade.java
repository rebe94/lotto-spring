package pl.lotto.resultchecker;

import java.time.LocalDate;
import java.time.ZonedDateTime;
import java.util.List;
import java.util.Set;

import org.apache.tomcat.jni.Local;
import org.springframework.scheduling.annotation.Scheduled;
import pl.lotto.lottonumbergenerator.LottoNumberGeneratorFacade;
import pl.lotto.numberreceiver.NumberReceiverFacade;
import pl.lotto.numberreceiver.dto.TicketsDto;
import pl.lotto.resultchecker.dto.WinnerDto;
import pl.lotto.resultchecker.dto.WinnersDto;

public class ResultCheckerFacade {

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
                        .drawingDate(winner.getDrawingDate())
                        .build())
                .toList();

        return WinnersDto.builder()
                .list(winnersDto)
                .build();
    }

    @Scheduled(cron = "*/30 * * * * *")
    //@Scheduled(cron = "0 5 19 * * *")
    public void checkWinnersAfterDraw() {
        Set<Integer> winningNumbers = lottoNumberGeneratorFacade.getWinningNumbers(LocalDate.now());
        TicketsDto ticketsDto = numberReceiverFacade.getAllTicketsByDrawingDate(LocalDate.now());
        List<Winner> winners = ticketsDto.getList().stream()
                .filter(ticketDto -> ticketDto.getNumbers().equals(winningNumbers))
                .map(ticketDto -> Winner.builder()
                        .hash(ticketDto.getHash())
                        .numbers(ticketDto.getNumbers())
                        .drawingDate(ticketDto.getDrawingDate())
                        .build())
                .toList();
        winnerRepository.saveAll(winners);
    }
}
