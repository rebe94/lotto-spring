package pl.lotto;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import pl.lotto.lottonumbergenerator.LottoNumberGeneratorFacade;
import pl.lotto.numberreceiver.NumberReceiverConfiguration;
import pl.lotto.numberreceiver.NumberReceiverFacade;
import pl.lotto.numberreceiver.ResultMessage;
import pl.lotto.resultannouncer.ResultAnnouncerConfiguration;
import pl.lotto.resultchecker.ResultCheckerConfiguration;
import pl.lotto.resultchecker.ResultCheckerFacade;

import java.time.LocalDate;
import java.util.Set;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;
import static org.junit.jupiter.api.Assertions.assertAll;
import static org.mockito.BDDMockito.given;

@Tag("SpringTest")
@SpringBootTest
class BaseIntegrationSpec {

    private final LocalDate SOME_DATE = LocalDate.of(2000, 1, 1);
    @MockBean
    private LottoNumberGeneratorFacade lottoNumberGeneratorFacade;
    @Autowired
    private NumberReceiverFacade numberReceiverFacade = new NumberReceiverConfiguration().numberReceiverFacadeForTests();
    @Autowired
    private ResultCheckerFacade resultCheckerFacade = new ResultCheckerConfiguration()
            .resultCheckerFacadeForTests(numberReceiverFacade, lottoNumberGeneratorFacade);
    @Autowired
    private pl.lotto.resultannouncer.ResultAnnouncerFacade resultAnnouncerFacade = new ResultAnnouncerConfiguration()
            .resultAnnouncerFacade(resultCheckerFacade);

    @Test
    @DisplayName("User chooses correct numbers and receives acceptance information and hash code." +
            "Next checks result using hash code and receives win information.")
    public void user_chooses_correct_numbers_and_receives_acceptance_and_hash_code_then_checks_result_and_receives_win_information() {
        final ResultMessage receivedUserMessage = userChoosesNumbers(Set.of(1, 2, 3, 4, 5, 6));
        final String GENERATED_HASH = receivedUserMessage.getHash();
        final String DRAW_DATE = receivedUserMessage.getDrawingDate();
        generatorDrawsWinningNumbers(Set.of(1, 2, 3, 4, 5, 6));
        final String checkResultMessage = userCheckResultByHash(GENERATED_HASH);

        final ResultMessage numbersAccepted = new ResultMessage("Accepted", GENERATED_HASH, DRAW_DATE);
        assertAll(
                () -> assertThat(receivedUserMessage, equalTo(numbersAccepted)),
                () -> assertThat(checkResultMessage, equalTo("Winner"))
        );
    }

    @Test
    @DisplayName("User chooses correct numbers and receives acceptance information and hash code." +
            "Next checks result using hash code and receives lose information.")
    public void user_chooses_correct_numbers_and_receives_acceptance_and_hash_code_then_checks_result_and_receives_lose_information() {
        final ResultMessage receivedUserMessage = userChoosesNumbers(Set.of(1, 2, 3, 4, 5, 6));
        final String GENERATED_HASH = receivedUserMessage.getHash();
        final String DRAW_DATE = receivedUserMessage.getDrawingDate();
        generatorDrawsWinningNumbers(Set.of(1, 2, 3, 4, 5, 15));
        final String checkResultMessage = userCheckResultByHash(GENERATED_HASH);

        final ResultMessage numbersAccepted = new ResultMessage("Accepted", GENERATED_HASH, DRAW_DATE);
        assertAll(
                () -> assertThat(receivedUserMessage, equalTo(numbersAccepted)),
                () -> assertThat(checkResultMessage, equalTo("Loser"))
        );
    }

    private ResultMessage userChoosesNumbers(Set<Integer> numbers) {
        return numberReceiverFacade.inputNumbers(numbers);
    }

    private void generatorDrawsWinningNumbers(Set<Integer> numbers) {
        given(lottoNumberGeneratorFacade.getWinningNumbers(SOME_DATE)).willReturn(numbers);
    }

    private String userCheckResultByHash(String generatedHash) {
        return resultAnnouncerFacade.checkResult(generatedHash);
    }
}