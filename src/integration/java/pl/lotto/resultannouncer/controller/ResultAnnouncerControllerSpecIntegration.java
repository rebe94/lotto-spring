package pl.lotto.resultannouncer.controller;

import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;
import pl.lotto.resultannouncer.ResultAnnouncerFacade;
import java.time.LocalDate;

import static org.hamcrest.Matchers.containsString;
import static org.mockito.BDDMockito.given;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static pl.lotto.resultannouncer.ResultAnnouncerMessageProvider.lose_message;
import static pl.lotto.resultannouncer.ResultAnnouncerMessageProvider.need_to_wait_message;
import static pl.lotto.resultannouncer.ResultAnnouncerMessageProvider.no_ticket_message;
import static pl.lotto.resultannouncer.ResultAnnouncerMessageProvider.win_message;

@Tag("SpringTest")
@SpringBootTest
@AutoConfigureMockMvc
class ResultAnnouncerControllerSpecIntegration {

    @MockBean
    private ResultAnnouncerFacade resultAnnouncerFacade;

    @Autowired
    private MockMvc mockMvc;

    @Test
    public void returns_ok_status_when_makes_announcer_request() throws Exception {
        mockMvc.perform(get("/announcer"))
                .andDo(print())
                .andExpect(status().isOk());
    }

    @Test
    public void returns_win_message_when_user_hits_winning_numbers() throws Exception {
        // given
        final String WIN_HASH = "win_hash";
        given(resultAnnouncerFacade.checkResult(WIN_HASH)).willReturn(win_message());
        // when
        // then
        mockMvc.perform(get("/announcer")
                        .param("hashCode", WIN_HASH))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().string(containsString(win_message())));
    }


    @Test
    public void returns_lose_message_when_user_does_not_hit_winning_numbers() throws Exception {
        // given
        final String LOSE_HASH = "lose_hash";
        given(resultAnnouncerFacade.checkResult(LOSE_HASH)).willReturn(lose_message());
        // when
        // then
        mockMvc.perform(get("/announcer")
                        .param("hashCode", LOSE_HASH))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().string(containsString(lose_message())));
    }

    @Test
    public void returns_no_ticket_message_when_user_checks_hash_code_which_does_not_exist() throws Exception {
        // given
        final String NOT_EXISTING_HASH = "not_existing_hash";
        given(resultAnnouncerFacade.checkResult(NOT_EXISTING_HASH)).willReturn(no_ticket_message());
        // when
        // then
        mockMvc.perform(get("/announcer")
                        .param("hashCode", NOT_EXISTING_HASH))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().string(containsString(no_ticket_message())));
    }

    @Test
    public void returns_need_to_wait_message_when_user_checks_hash_code_before_draw() throws Exception {
        // given
        final String BEFORE_DRAW_HASH = "before_draw_hash";
        final LocalDate DRAW_DATE = LocalDate.of(2000, 1, 1);
        given(resultAnnouncerFacade.checkResult(BEFORE_DRAW_HASH)).willReturn(need_to_wait_message(DRAW_DATE));
        // when
        // then
        mockMvc.perform(get("/announcer")
                        .param("hashCode", BEFORE_DRAW_HASH))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().string(containsString(need_to_wait_message(DRAW_DATE))));
    }
}