package pl.lotto.numberreceiver;

public class NumberReceiverConfiguration {

    public NumberReceiverFacade numberReceiverFacade(NumberValidator numberValidator,
                                                     NumberRepository numberRepository) {
        return new NumberReceiverFacade(numberValidator, numberRepository);
    }
}
