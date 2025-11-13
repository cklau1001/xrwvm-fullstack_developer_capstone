package io.cklau1001.capstone.model;

import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;


@Slf4j
public class CarTypeTest {

    @Test
    void getCarTypeByNameTest() {

        List<Map<String, Object>> testcases = List.of(
                Map.of("input", "SUV", "expected", CarType.SUV),
                Map.of("input", "Sedan", "expected", CarType.SEDAN),
                Map.of("input", "Wagon", "expected", CarType.WAGON)
        );

        for (Map<String, Object> test: testcases) {
            CarType carType = CarType.getCarTypeByName((String) test.get("input")).orElse(null);
            assertThat(carType).as("carType should not be null").isNotNull();
            assertThat(carType).isEqualTo((CarType) test.get("expected"));
        }

    }

}
