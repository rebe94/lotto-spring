package pl.lotto.numberreceiver;

import pl.lotto.configuration.GameConfiguration;
import pl.lotto.numberreceiver.dto.ResultMessageDto;

public class NumberReceiverMessageProvider {

    public static ResultMessageDto accepted(Ticket ticket) {
        return new ResultMessageDto("Accepted.", ticket.getHash(), ticket.getDrawDate());
    }

    public static ResultMessageDto failed() {
        return new ResultMessageDto("Wrong amount of numbers or numbers out of range.", "False", GameConfiguration.nextDrawDate());
    }

    public static ResultMessageDto input_error() {
        return new ResultMessageDto("Wrong input. Numbers must be integers separated by spaces.", "False", GameConfiguration.nextDrawDate());
    }
}
