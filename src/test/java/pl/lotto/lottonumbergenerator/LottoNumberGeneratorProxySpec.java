package pl.lotto.lottonumbergenerator;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import pl.lotto.lottonumbergenerator.dto.WinningNumbers;

import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.greaterThanOrEqualTo;
import static org.hamcrest.Matchers.lessThanOrEqualTo;
import static org.junit.jupiter.api.Assertions.assertAll;
import static pl.lotto.configuration.GameConfiguration.AMOUNT_OF_NUMBERS;
import static org.hamcrest.MatcherAssert.assertThat;
import static pl.lotto.configuration.GameConfiguration.HIGHEST_NUMBER;
import static pl.lotto.configuration.GameConfiguration.LOWEST_NUMBER;

import java.util.Set;
import java.util.TreeSet;

@SpringBootTest
class LottoNumberGeneratorProxySpec {

    @Autowired
    LottoNumberGeneratorProxy lottoNumberGeneratorProxy;

    @Test
    @DisplayName("Should send request to lotto number generator service and receive winning numbers according to generate configuration")
    public void should_send_request_to_lotto_number_generator_service_and_receive_winning_numbers_according_to_generate_configuration() {
        // given
        // when
        WinningNumbers winningNumbersJSON = lottoNumberGeneratorProxy.generateNumbers();
        Set<Integer> receivedNumbers = new TreeSet<>(winningNumbersJSON.getWinningNumbers());
        System.out.println(receivedNumbers);

        // then
        assertAll(
                () -> assertThat(receivedNumbers.size(), equalTo(AMOUNT_OF_NUMBERS)),
                () -> assertThat(receivedNumbers.stream().max(Integer::compareTo).get(), lessThanOrEqualTo(HIGHEST_NUMBER)),
                () -> assertThat(receivedNumbers.stream().min(Integer::compareTo).get(), greaterThanOrEqualTo(LOWEST_NUMBER))
        );
    }

}