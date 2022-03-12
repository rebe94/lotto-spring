package pl.lotto.numberreceiver;

import lombok.AccessLevel;
import lombok.Builder;
import lombok.Value;

@Value
public class ResultMessage {
    String message;
    String hash;
    String drawingDate;
}