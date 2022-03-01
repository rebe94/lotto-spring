package pl.lotto;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import pl.lotto.lottonumbergenerator.LottoNumberGeneratorFacade;
import pl.lotto.numberreceiver.NumberReceiverConfiguration;
import pl.lotto.numberreceiver.NumberReceiverFacade;
import pl.lotto.numberreceiver.ResultMessage;
import pl.lotto.resultannouncer.ResultAnnouncerConfiguration;
import pl.lotto.resultannouncer.ResultAnnouncerFacade;
import pl.lotto.resultchecker.ResultCheckerConfiguration;
import pl.lotto.resultchecker.ResultCheckerFacade;

import java.util.Set;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;
import static org.junit.jupiter.api.Assertions.assertAll;
import static org.mockito.BDDMockito.given;

@SpringBootTest
class AppRunnerIntegrationTests {

    @MockBean
    private LottoNumberGeneratorFacade lottoNumberGeneratorFacade;

    @Autowired
    private NumberReceiverFacade numberReceiverFacade = new NumberReceiverConfiguration().numberReceiverFacadeForTests();
    @Autowired
    private ResultCheckerFacade resultCheckerFacade = new ResultCheckerConfiguration()
            .resultCheckerFacadeForTests(numberReceiverFacade, lottoNumberGeneratorFacade);
    @Autowired
    private ResultAnnouncerFacade resultAnnouncerFacade = new ResultAnnouncerConfiguration()
            .resultAnnouncerFacade(resultCheckerFacade);

    /*@Test
    @DisplayName("User chooses correct numbers and receives acceptance information and hash code." +
            "Next checks result using hash code and receives win information.")
    public void user_chooses_correct_numbers_and_receives_acceptance_and_hash_code_then_checks_result_and_receives_win_information() {
        final Set<Integer> userNumbers = Set.of(1, 2, 3, 4, 5, 6);
        final Set<Integer> winningNumbers = Set.of(1, 2, 3, 4, 5, 6);
        given(lottoNumberGeneratorFacade.getWinningNumbers()).willReturn(winningNumbers);

        final ResultMessage receivedUserMessage = numberReceiverFacade.inputNumbers(userNumbers);
        final String GENERATED_HASH = receivedUserMessage.getHash();
        final String resultUserMessage = resultAnnouncerFacade.checkResult(GENERATED_HASH);

        final ResultMessage numbersAccepted = new ResultMessage("Accepted", GENERATED_HASH);

        assertAll(
                () -> assertThat(receivedUserMessage, equalTo(numbersAccepted)),
                () -> assertThat(resultUserMessage, equalTo("Winner"))
        );
    }*/

    @Test
    @DisplayName("User chooses correct numbers and receives acceptance information and hash code." +
            "Next checks result using hash code and receives win information.")
    public void user_chooses_correct_numbers_and_receives_acceptance_and_hash_code_then_checks_result_and_receives_win_information() {
        final ResultMessage receivedUserMessage = userChoosesNumbers(Set.of(1, 2, 3, 4, 5, 6));
        String GENERATED_HASH = receivedUserMessage.getHash();
        generatorDrawsWinningNumbers(Set.of(1, 2, 3, 4, 5, 6));
        final String checkResultMessage = userCheckResultByHash(GENERATED_HASH);

        final ResultMessage numbersAccepted = new ResultMessage("Accepted", GENERATED_HASH);

        assertAll(
                () -> assertThat(receivedUserMessage, equalTo(numbersAccepted)),
                () -> assertThat(checkResultMessage, equalTo("Winner"))
        );
    }

   /* @Test
    @DisplayName("User chooses correct numbers and receives acceptance information and hash code." +
            "Next checks result using hash code and receives lose information.")
    public void user_chooses_correct_numbers_and_receives_acceptance_and_hash_code_then_checks_result_and_receives_lose_information() {
        final Set<Integer> userNumbers = Set.of(1, 2, 3, 4, 5, 6);
        final Set<Integer> winningNumbers = Set.of(1, 2, 3, 4, 5, 15);
        given(lottoNumberGeneratorFacade.getWinningNumbers()).willReturn(winningNumbers);

        final ResultMessage receivedUserMessage = numberReceiverFacade.inputNumbers(userNumbers);
        final String GENERATED_HASH = receivedUserMessage.getHash();
        final String checkResultMessage = resultAnnouncerFacade.checkResult(GENERATED_HASH);

        final ResultMessage numbersAccepted = new ResultMessage("Accepted", GENERATED_HASH);

        assertAll(
                () -> assertThat(receivedUserMessage, equalTo(numbersAccepted)),
                () -> assertThat(checkResultMessage, equalTo("Loser"))
        );
    }*/

    @Test
    @DisplayName("User chooses correct numbers and receives acceptance information and hash code." +
            "Next checks result using hash code and receives lose information.")
    public void user_chooses_correct_numbers_and_receives_acceptance_and_hash_code_then_checks_result_and_receives_lose_information() {
        final ResultMessage receivedUserMessage = userChoosesNumbers(Set.of(1, 2, 3, 4, 5, 6));
        String GENERATED_HASH = receivedUserMessage.getHash();
        generatorDrawsWinningNumbers(Set.of(1, 2, 3, 4, 5, 15));
        final String checkResultMessage = userCheckResultByHash(GENERATED_HASH);

        final ResultMessage numbersAccepted = new ResultMessage("Accepted", GENERATED_HASH);

        assertAll(
                () -> assertThat(receivedUserMessage, equalTo(numbersAccepted)),
                () -> assertThat(checkResultMessage, equalTo("Loser"))
        );
    }

    private ResultMessage userChoosesNumbers(Set<Integer> numbers) {
        return numberReceiverFacade.inputNumbers(numbers);
    }

    private void generatorDrawsWinningNumbers(Set<Integer> numbers) {
        given(lottoNumberGeneratorFacade.getWinningNumbers()).willReturn(numbers);
    }

    private String userCheckResultByHash(String generatedHash) {
        return resultAnnouncerFacade.checkResult(generatedHash);
    }
}