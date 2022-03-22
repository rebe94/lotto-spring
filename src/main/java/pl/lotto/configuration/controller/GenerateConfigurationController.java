package pl.lotto.configuration.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import pl.lotto.configuration.dto.GenerateConfigurationDto;

import static pl.lotto.configuration.GameConfiguration.AMOUNT_OF_NUMBERS;
import static pl.lotto.configuration.GameConfiguration.HIGHEST_NUMBER;
import static pl.lotto.configuration.GameConfiguration.LOWEST_NUMBER;

@RestController
class GenerateConfigurationController {

    @GetMapping("/configuration")
    public ResponseEntity<GenerateConfigurationDto> configuration() {
        GenerateConfigurationDto generateConfigurationDto = new GenerateConfigurationDto(AMOUNT_OF_NUMBERS, LOWEST_NUMBER, HIGHEST_NUMBER);
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(generateConfigurationDto);
    }
}
