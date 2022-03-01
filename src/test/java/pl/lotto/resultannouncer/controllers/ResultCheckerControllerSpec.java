package pl.lotto.resultannouncer.controllers;

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
class ResultCheckerControllerSpec {

    @Autowired
    private MockMvc mockMvc;

    @Test
    @DisplayName("Should return ok status when make checker request")
    public void should_return_ok_status_when_make_checker_request() throws Exception {
        mockMvc.perform(get("/checker"))
                .andDo(print())
                .andExpect(status().isOk());
    }
}