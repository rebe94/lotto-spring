package pl.lotto.numberreceiver.dto;

import lombok.Builder;
import lombok.Value;

import java.time.LocalDate;

@Value
public class ResultMessageDto {

    String message;
    String hash;
    LocalDate drawDate;
}