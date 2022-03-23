package pl.lotto.numberreceiver.controller;

import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;
import pl.lotto.numberreceiver.NumberReceiverFacade;
import pl.lotto.numberreceiver.NumberReceiverMessageProvider;
import pl.lotto.numberreceiver.Ticket;
import pl.lotto.numberreceiver.dto.ResultMessageDto;
import java.util.Set;

import static org.hamcrest.Matchers.containsString;
import static org.mockito.BDDMockito.given;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@Tag("SpringTest")
@SpringBootTest
@AutoConfigureMockMvc
//@WebMvcTest(controllers = NumberReceiverController.class)
class NumberReceiverControllerSpec {//extends BaseIntegrationSpec {

    @MockBean
    private NumberReceiverFacade numberReceiverFacade;

    @Autowired
    private MockMvc mockMvc;
    @Autowired
    private NumberReceiverController numberReceiverController;

    @Test
    public void returns_ok_status_when_makes_receiver_request() throws Exception {
        mockMvc.perform(get("/receiver"))
                .andDo(print())
                .andExpect(status().isOk());
    }

    @Test
    public void returns_accepted_message_when_sends_correct_numbers() throws Exception {
        // given
        final String input = "1 2 3 4 5 6";
        final Set<Integer> numbers = numberReceiverController.parseToSetOfNumbers(input);
        final Ticket ticket = Ticket.builder().build();
        final ResultMessageDto accepted = NumberReceiverMessageProvider.accepted(ticket);
        given(numberReceiverFacade.inputNumbers(numbers)).willReturn(accepted);

        // when
        // then
        mockMvc.perform(post("/receiver")
                        .param("input", input))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().string(containsString(accepted.getMessage())));
    }

    @Test
    public void returns_failed_message_when_sends_incorrect_numbers() throws Exception {
        final String input = "1 2 3 4 5 100";
        final Set<Integer> numbers = numberReceiverController.parseToSetOfNumbers(input);
        final ResultMessageDto failed = NumberReceiverMessageProvider.failed();
        given(numberReceiverFacade.inputNumbers(numbers)).willReturn(failed);

        mockMvc.perform(post("/receiver")
                        .param("input", input))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().string(containsString(failed.getMessage())));
    }

    @Test
    public void returns_failed_message_when_sends_wrong_input() throws Exception {
        // given
        final String input = "1, 2 3 4 5 6";
        final Set<Integer> numbers = numberReceiverController.parseToSetOfNumbers(input);
        final ResultMessageDto input_error = NumberReceiverMessageProvider.input_error();
        given(numberReceiverFacade.inputNumbers(numbers)).willReturn(input_error);

        // when
        // then
        mockMvc.perform(post("/receiver")
                        .param("input", input))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().string(containsString(input_error.getMessage())));
    }
}