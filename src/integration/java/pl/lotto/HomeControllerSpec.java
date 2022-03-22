package pl.lotto;

import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.redirectedUrl;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@Tag("SpringTest")
@SpringBootTest
@AutoConfigureMockMvc
class HomeControllerSpec extends BaseIntegrationSpec {

    @Autowired
    private MockMvc mockMvc;

    @Test
    public void returns_ok_status_when_makes_home_request() throws Exception {
        mockMvc.perform(get("/home"))
                .andDo(print())
                .andExpect(status().isOk());
    }

    @Test
    public void redirects_to_home_page_when_makes_empty_path_request() throws Exception {
        mockMvc.perform(get("/"))
                .andDo(print())
                .andExpect(redirectedUrl("home"))
                .andExpect(status().isFound());
    }
}