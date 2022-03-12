package pl.lotto;

import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.TestPropertySource;

@SpringBootTest(
        classes = AppRunner.class,
        webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT,
        properties = "application.environment=integration"
        )
@TestPropertySource(locations="test.properties")
abstract public class BaseIntegrationSpec {
}
