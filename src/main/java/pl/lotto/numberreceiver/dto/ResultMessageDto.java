package pl.lotto.numberreceiver.dto;

import lombok.Value;

import java.time.LocalDate;

@Value
public class ResultMessageDto {

    private String message;
    private String hash;
    private LocalDate drawDate;
}