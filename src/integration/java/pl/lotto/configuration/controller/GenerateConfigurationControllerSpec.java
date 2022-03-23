package pl.lotto.configuration.controller;

import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.web.servlet.MockMvc;

import static org.hamcrest.Matchers.is;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static pl.lotto.configuration.GameConfiguration.AMOUNT_OF_NUMBERS;
import static pl.lotto.configuration.GameConfiguration.HIGHEST_NUMBER;
import static pl.lotto.configuration.GameConfiguration.LOWEST_NUMBER;

@Tag("SpringTest")
@SpringBootTest
@AutoConfigureMockMvc
class GenerateConfigurationControllerSpec {

    @Autowired
    private MockMvc mockMvc;

    @Test
    public void returns_correct_values_of_generate_configuration_when_it_is_requested() throws Exception {
        mockMvc.perform(get("/configuration"))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.amountOfNumbers", is(AMOUNT_OF_NUMBERS)))
                .andExpect(jsonPath("$.lowestNumber", is(LOWEST_NUMBER)))
                .andExpect(jsonPath("$.highestNumber", is(HIGHEST_NUMBER)));
    }
}
