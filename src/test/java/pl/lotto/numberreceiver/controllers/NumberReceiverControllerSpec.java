package pl.lotto.numberreceiver.controllers;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@Tag("SpringTest")
@SpringBootTest
@AutoConfigureMockMvc
class NumberReceiverControllerSpec {

    @Autowired
    private MockMvc mockMvc;

    @Test
    @DisplayName("Should return ok status when make receiver request")
    public void should_return_ok_status_when_make_receiver_request() throws Exception {
        mockMvc.perform(get("/receiver"))
                .andDo(print())
                .andExpect(status().isOk());
    }
}