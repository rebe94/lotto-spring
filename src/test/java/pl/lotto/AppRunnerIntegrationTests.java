package pl.lotto;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.ApplicationContext;
import pl.lotto.lottonumbergenerator.LottoNumberGeneratorFacade;
import pl.lotto.numberreceiver.NumberReceiverFacade;
import pl.lotto.numberreceiver.ResultMessage;
import pl.lotto.resultannouncer.ResultAnnouncerFacade;

import java.util.Set;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;
import static org.junit.jupiter.api.Assertions.assertAll;
import static org.mockito.BDDMockito.given;

@SpringBootTest
class AppRunnerIntegrationTests {

    @MockBean
    LottoNumberGeneratorFacade lottoNumberGeneratorFacade;

    @Autowired
    private ApplicationContext context;

    @Test
    @DisplayName("User chooses correct numbers and receives acceptance information and hash code." +
            "Next checks result using hash code and receives win information.")
    public void user_chooses_correct_numbers_and_receives_acceptance_and_hash_code_then_checks_result_and_receives_win_information() {
        // given
        NumberReceiverFacade numberReceiverFacade = context.getBean(NumberReceiverFacade.class);
        ResultAnnouncerFacade resultAnnouncerFacade = context.getBean(ResultAnnouncerFacade.class);

        final Set<Integer> userNumbers = Set.of(1, 2, 3, 4, 5, 6);

        given(lottoNumberGeneratorFacade.winningNumbers()).willReturn(Set.of(1, 2, 3, 4, 5, 6));
        System.out.println("Po nadpisaniu metody w testach: " + lottoNumberGeneratorFacade.winningNumbers());
        // when
        final ResultMessage receivedUserMessage = numberReceiverFacade.inputNumbers(userNumbers);
        final String SOME_HASH = receivedUserMessage.getHash();

        final String resultUserMessage = resultAnnouncerFacade.checkResult(SOME_HASH);

        // then
        assertAll(
                () -> assertThat(receivedUserMessage, equalTo(new ResultMessage("Accepted", SOME_HASH))),
                () -> assertThat(resultUserMessage, equalTo("Winner"))
        );
    }

    @Test
    @DisplayName("User chooses correct numbers and receives acceptance information and hash code." +
            "Next checks result using hash code and receives lose information.")
    public void user_chooses_correct_numbers_and_receives_acceptance_and_hash_code_then_checks_result_and_receives_lose_information() {
        // given
        NumberReceiverFacade numberReceiverFacade = context.getBean(NumberReceiverFacade.class);
        ResultAnnouncerFacade resultAnnouncerFacade = context.getBean(ResultAnnouncerFacade.class);

        final Set<Integer> userNumbers = Set.of(1, 2, 3, 4, 5, 6);
        given(lottoNumberGeneratorFacade.winningNumbers()).willReturn(Set.of(1, 2, 3, 4, 5, 15));
        System.out.println("Po nadpisaniu metody w testach: " + lottoNumberGeneratorFacade.winningNumbers());
        // when
        final ResultMessage receivedUserMessage = numberReceiverFacade.inputNumbers(userNumbers);
        final String SOME_HASH = receivedUserMessage.getHash();

        final String resultUserMessage = resultAnnouncerFacade.checkResult(SOME_HASH);

        // then
        assertAll(
                () -> assertThat(receivedUserMessage, equalTo(new ResultMessage("Accepted", SOME_HASH))),
                () -> assertThat(resultUserMessage, equalTo("Loser"))
        );
    }
}