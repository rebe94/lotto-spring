package pl.lotto.resultannouncer.controller;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import org.mockito.MockedStatic;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;
import pl.lotto.BaseIntegrationSpec;
import pl.lotto.configuration.GameConfiguration;
import pl.lotto.lottonumbergenerator.LottoNumberGeneratorFacade;
import pl.lotto.lottonumbergenerator.dto.WinningNumbersDto;
import pl.lotto.numberreceiver.NumberReceiverFacade;
import pl.lotto.numberreceiver.TicketRepository;
import pl.lotto.numberreceiver.dto.ResultMessageDto;
import pl.lotto.resultannouncer.ResultAnnouncerFacade;
import pl.lotto.resultchecker.ResultCheckerFacade;
import pl.lotto.resultchecker.WinnerRepository;

import java.time.LocalDate;
import java.util.Set;

import static org.hamcrest.Matchers.containsString;
import static org.mockito.BDDMockito.given;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@Tag("SpringTest")
@SpringBootTest
@AutoConfigureMockMvc
class ResultAnnouncerControllerSpec extends BaseIntegrationSpec {

    private final LocalDate NEXT_DRAW_DATE = LocalDate.of(2000, 1, 1);

    @MockBean
    private LottoNumberGeneratorFacade lottoNumberGeneratorFacade;

    @Autowired
    private MockMvc mockMvc;
    @Autowired
    private NumberReceiverFacade numberReceiverFacade;
    @Autowired
    private ResultCheckerFacade resultCheckerFacade;
    @Autowired
    private TicketRepository ticketRepository;
    @Autowired
    private WinnerRepository winnerRepository;

    @AfterEach
    void tearDown() {
        ticketRepository.deleteAll();
        winnerRepository.deleteAll();
    }

    @Test
    public void returns_ok_status_when_makes_announcer_request() throws Exception {
        mockMvc.perform(get("/announcer"))
                .andDo(print())
                .andExpect(status().isOk());
    }

    @Test
    public void returns_win_message_when_user_hits_winning_numbers_and_then_checks_if_has_win() throws Exception {
        try (MockedStatic<GameConfiguration> mocked = Mockito.mockStatic(GameConfiguration.class)) {
            // given
            mocked.when(GameConfiguration::nextDrawDate).thenReturn(NEXT_DRAW_DATE);
            Set<Integer> userNumbers = Set.of(1, 2, 3, 4, 5, 6);
            Set<Integer> winningNumbers = Set.of(1, 2, 3, 4, 5, 6);
            ResultMessageDto result = numberReceiverFacade.inputNumbers(winningNumbers);
            System.out.println(result.getDrawDate());
            System.out.println("next: " + GameConfiguration.nextDrawDate());
            WinningNumbersDto winningNumbersDto = WinningNumbersDto.builder()
                    .winningNumbers(userNumbers)
                    .validationMessage(WinningNumbersDto.ValidationMessage.VALID)
                    .build();
            given(lottoNumberGeneratorFacade.getWinningNumbers(result.getDrawDate())).willReturn(winningNumbersDto);
            System.out.println(result.getDrawDate());
            System.out.println("next: " + GameConfiguration.nextDrawDate());
            mocked.when(GameConfiguration::nextDrawDate).thenReturn(NEXT_DRAW_DATE.plusDays(1));
            System.out.println("next: " + GameConfiguration.nextDrawDate());
            // when
            // then
            resultCheckerFacade.checkWinnersAfterDraw(result.getDrawDate());
            mockMvc.perform(get("/announcer")
                            .param("hashCode", result.getHash()))
                    .andDo(print())
                    .andExpect(status().isOk())
                    .andExpect(content().string(containsString(ResultAnnouncerFacade.win_message())));
        }
    }

    @Test
    public void returns_lose_message_when_user_does_not_hit_winning_numbers_and_then_checks_if_has_win() throws Exception {
        try (MockedStatic<GameConfiguration> mocked = Mockito.mockStatic(GameConfiguration.class)) {
            // given
            mocked.when(GameConfiguration::nextDrawDate).thenReturn(NEXT_DRAW_DATE);

            Set<Integer> userNumbers = Set.of(1, 2, 3, 4, 5, 6);
            Set<Integer> winningNumbers = Set.of(1, 2, 3, 4, 5, 10);
            ResultMessageDto result = numberReceiverFacade.inputNumbers(winningNumbers);
            WinningNumbersDto winningNumbersDto = WinningNumbersDto.builder()
                    .winningNumbers(userNumbers)
                    .validationMessage(WinningNumbersDto.ValidationMessage.VALID)
                    .build();
            given(lottoNumberGeneratorFacade.getWinningNumbers(result.getDrawDate())).willReturn(winningNumbersDto);
            mocked.when(GameConfiguration::nextDrawDate).thenReturn(NEXT_DRAW_DATE.plusDays(1));
            // when
            // then
            resultCheckerFacade.checkWinnersAfterDraw(result.getDrawDate());
            mockMvc.perform(get("/announcer")
                            .param("hashCode", result.getHash()))
                    .andDo(print())
                    .andExpect(status().isOk())
                    .andExpect(content().string(containsString(ResultAnnouncerFacade.lose_message())));
        }
    }

    @Test
    public void returns_no_ticket_message_when_user_checks_hash_code_which_does_not_exist() throws Exception {
        // given
        // when
        // then
        mockMvc.perform(get("/announcer")
                        .param("hashCode", "not_existing_hash_code"))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().string(containsString(ResultAnnouncerFacade.no_ticket_message())));
    }
}