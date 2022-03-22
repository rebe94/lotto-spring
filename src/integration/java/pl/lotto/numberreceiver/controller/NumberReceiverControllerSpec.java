package pl.lotto.numberreceiver.controller;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.web.servlet.MockMvc;
import pl.lotto.BaseIntegrationSpec;
import pl.lotto.numberreceiver.NumberReceiverValidationResult;
import pl.lotto.numberreceiver.Ticket;
import pl.lotto.numberreceiver.TicketRepository;
import pl.lotto.numberreceiver.dto.ResultMessageDto;

import static org.hamcrest.Matchers.containsString;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@Tag("SpringTest")
@SpringBootTest
@AutoConfigureMockMvc
class NumberReceiverControllerSpec extends BaseIntegrationSpec {

    @Autowired
    private MockMvc mockMvc;
    @Autowired
    private TicketRepository ticketRepository;

    @AfterEach
    void tearDown() {
        ticketRepository.deleteAll();
    }

    @Test
    public void returns_ok_status_when_makes_receiver_request() throws Exception {
        mockMvc.perform(get("/receiver"))
                .andDo(print())
                .andExpect(status().isOk());
    }

    @Test
    public void returns_accepted_message_when_sends_correct_numbers() throws Exception {
        String input = "1 2 3 4 5 6";
        Ticket ticket = Ticket.builder().build();
        ResultMessageDto accepted = NumberReceiverValidationResult.accepted(ticket);

        mockMvc.perform(post("/receiver")
                        .param("input", input))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().string(containsString(accepted.getMessage())));
    }

    @Test
    public void returns_failed_message_when_sends_incorrect_numbers() throws Exception {
        String input = "1 2 3 4 5 100";
        ResultMessageDto failed = NumberReceiverValidationResult.failed();

        mockMvc.perform(post("/receiver")
                        .param("input", input))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().string(containsString(failed.getMessage())));
    }

    @Test
    public void returns_failed_message_when_sends_wrong_input() throws Exception {
        String input = "1, 2 3 4 5 6";
        ResultMessageDto failed = NumberReceiverValidationResult.failed();

        mockMvc.perform(post("/receiver")
                        .param("input", input))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().string(containsString(failed.getMessage())));
    }
}