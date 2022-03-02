package pl.lotto.lottonumbergenerator;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import pl.lotto.lottonumbergenerator.dto.WinningNumbersDto;

import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.greaterThanOrEqualTo;
import static org.hamcrest.Matchers.lessThanOrEqualTo;
import static org.junit.jupiter.api.Assertions.assertAll;
import static org.mockito.BDDMockito.given;
import static pl.lotto.configuration.GameConfiguration.AMOUNT_OF_NUMBERS;
import static org.hamcrest.MatcherAssert.assertThat;
import static pl.lotto.configuration.GameConfiguration.HIGHEST_NUMBER;
import static pl.lotto.configuration.GameConfiguration.LOWEST_NUMBER;

import java.time.LocalDate;
import java.util.Set;

@Tag("SpringTest")
@SpringBootTest
class LottoNumberGeneratorFacadeIntegrationSpec {

    private final WinningNumbersDto winningNumbersDto = new WinningNumbersDto(Set.of(1,2,3,4,5,6));
    private final LocalDate SOME_DATE = LocalDate.of(2000,1,1);
    @MockBean
    private LottoNumberGenerator lottoNumberGenerator;
    @Autowired
    private LottoNumberGeneratorFacade lottoNumberGeneratorFacade;

    @Test
    @DisplayName("Should send request to lotto number generator service and receive winning numbers according to generate configuration")
    public void should_send_request_to_lotto_number_generator_service_and_receive_winning_numbers_according_to_generate_configuration() {
        // given
        given(lottoNumberGenerator.getWinningNumbers(SOME_DATE)).willReturn(winningNumbersDto);
        // when
        Set<Integer> winningNumbers = lottoNumberGeneratorFacade.getWinningNumbers(SOME_DATE);
        System.out.println(winningNumbers);

        // then
        assertAll(
                () -> assertThat(winningNumbers.size(), equalTo(AMOUNT_OF_NUMBERS)),
                () -> assertThat(winningNumbers.stream().max(Integer::compareTo).get(), lessThanOrEqualTo(HIGHEST_NUMBER)),
                () -> assertThat(winningNumbers.stream().min(Integer::compareTo).get(), greaterThanOrEqualTo(LOWEST_NUMBER))
        );
    }

}