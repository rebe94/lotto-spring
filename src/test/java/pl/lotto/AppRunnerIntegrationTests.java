package pl.lotto;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import pl.lotto.lottonumbergenerator.LottoNumberGeneratorFacade;
import pl.lotto.numberreceiver.InMemoryNumberRepository;
import pl.lotto.numberreceiver.NumberReceiverConfiguration;
import pl.lotto.numberreceiver.NumberReceiverFacade;
import pl.lotto.numberreceiver.NumberValidatorImpl;
import pl.lotto.numberreceiver.ResultMessage;
import pl.lotto.resultannouncer.ResultAnnouncerConfiguration;
import pl.lotto.resultannouncer.ResultAnnouncerFacade;
import pl.lotto.resultchecker.InMemoryWinnersRepository;
import pl.lotto.resultchecker.ResultCheckerConfiguration;
import pl.lotto.resultchecker.ResultCheckerFacade;

import java.util.Set;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;
import static org.junit.jupiter.api.Assertions.assertAll;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.mock;

//@SpringBootTest
class AppRunnerIntegrationTests {

    @Test
    @DisplayName("User chooses correct numbers and receives acceptance information and hash code." +
            "Next checks result using hash code and receives win information.")
    public void user_chooses_correct_numbers_and_receives_acceptance_and_hash_code_then_checks_result_and_receives_win_information() {
        // given
        final Set<Integer> userNumbers = Set.of(1, 2, 3, 4, 5, 6);
        final NumberReceiverFacade numberReceiverFacade; /*= new NumberReceiverConfiguration()
                .numberReceiverFacade(new NumberValidatorImpl(), new InMemoryNumberRepository());*/

        final LottoNumberGeneratorFacade lottoNumberGeneratorFacade =
                mock(LottoNumberGeneratorFacade.class);
        given(lottoNumberGeneratorFacade.winningNumbers()).willReturn(Set.of(1, 2, 3, 4, 5, 6));
        System.out.println("Po nadpisaniu metody w testach: " + lottoNumberGeneratorFacade.winningNumbers());
        final ResultCheckerFacade resultCheckerFacade = new ResultCheckerConfiguration().resultCheckerFacade(new InMemoryWinnersRepository(),
                numberReceiverFacade,
                lottoNumberGeneratorFacade);
        // when
        final ResultMessage receivedUserMessage = numberReceiverFacade.inputNumbers(userNumbers);
        final String SOME_HASH = receivedUserMessage.getHash();

        final ResultAnnouncerFacade resultAnnouncerFacade = new ResultAnnouncerConfiguration()
                .resultAnnouncerFacade(resultCheckerFacade);
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
        final Set<Integer> userNumbers = Set.of(1, 2, 3, 4, 5, 6);
        final NumberReceiverFacade numberReceiverFacade = new NumberReceiverConfiguration()
                .numberReceiverFacade(new NumberValidatorImpl(), new InMemoryNumberRepository());

        final LottoNumberGeneratorFacade lottoNumberGeneratorFacade =
                mock(LottoNumberGeneratorFacade.class);
        given(lottoNumberGeneratorFacade.winningNumbers()).willReturn(Set.of(1, 2, 3, 4, 5, 15));
        System.out.println("Po nadpisaniu metody w testach: " + lottoNumberGeneratorFacade.winningNumbers());
        final ResultCheckerFacade resultCheckerFacade = new ResultCheckerConfiguration()
                .resultCheckerFacade(new InMemoryWinnersRepository(), numberReceiverFacade,
                        lottoNumberGeneratorFacade);
        // when
        final ResultMessage receivedUserMessage = numberReceiverFacade.inputNumbers(userNumbers);
        final String SOME_HASH = receivedUserMessage.getHash();

        final ResultAnnouncerFacade resultAnnouncerFacade = new ResultAnnouncerConfiguration()
                .resultAnnouncerFacade(resultCheckerFacade);
        final String resultUserMessage = resultAnnouncerFacade.checkResult(SOME_HASH);

        // then
        assertAll(
                () -> assertThat(receivedUserMessage, equalTo(new ResultMessage("Accepted", SOME_HASH))),
                () -> assertThat(resultUserMessage, equalTo("Loser"))
        );
    }
}