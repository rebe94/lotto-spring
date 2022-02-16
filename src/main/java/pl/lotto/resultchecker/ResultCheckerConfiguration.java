package pl.lotto.resultchecker;

import pl.lotto.lottonumbergenerator.LottoNumberGeneratorFacade;
import pl.lotto.numberreceiver.NumberReceiverFacade;

public class ResultCheckerConfiguration {

    public ResultCheckerFacade resultCheckerFacade(WinnersRepository winnersRepository,
                                                   NumberReceiverFacade numberReceiverFacade,
                                                   LottoNumberGeneratorFacade lottoNumberGeneratorFacade) {
        return new ResultCheckerFacade(winnersRepository,
                numberReceiverFacade,
                lottoNumberGeneratorFacade);
    }
}
