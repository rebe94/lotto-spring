package pl.lotto.proxy;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.Set;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class GenerateNumbersProxySpec {

    @Autowired
    private GenerateNumbersProxy generateNumbersProxy;

    @Test
    public void a() {

        Set<Integer> receivedNumbers = generateNumbersProxy.generateNumbers();
        System.out.println(receivedNumbers);

    }

}