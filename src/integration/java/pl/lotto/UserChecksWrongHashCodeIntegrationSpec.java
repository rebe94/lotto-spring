package pl.lotto;

import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import pl.lotto.resultannouncer.ResultAnnouncerFacade;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;
import static pl.lotto.resultannouncer.ResultAnnouncerMessageProvider.no_ticket_message;

@Tag("SpringTest")
@SpringBootTest
class UserChecksWrongHashCodeIntegrationSpec extends BaseIntegrationSpec {

    @Autowired
    private ResultAnnouncerFacade resultAnnouncerFacade;

    @Test
    public void user_chooses_correct_numbers_and_receives_acceptance_and_hash_code_then_checks_result_and_receives_win_information() {
        // given
        // when
        final String checkResultMessage = resultAnnouncerFacade.checkResult("not_existing_hash_code");
        // then
        assertThat(checkResultMessage, equalTo(no_ticket_message()));
    }
}
