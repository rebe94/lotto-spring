package pl.lotto.lottonumbergenerator;

public class LottoNumberGeneratorConfiguration {

    public LottoNumberGeneratorFacade lottoNumberGeneratorFacade() {
        return new LottoNumberGeneratorFacade();
    }
}
