package io.cklau1001.capstone.model;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;

import java.util.HashMap;
import java.util.Map;
import java.util.NoSuchElementException;
import java.util.Optional;

@RequiredArgsConstructor
@Getter
public enum CarType {

    SEDAN("Sedan"),
    SUV("SUV"),
    WAGON("Wagon");

    private final String type;

    final private static Map<String, CarType> map;

    static {
        map = new HashMap<>();
        for (CarType ctype: CarType.values()) {
            map.put(ctype.getType(), ctype);
        }
    }

    /**
     * Look up the CarType from the name provided.
     *
     * @param name
     * @return CarType for a valid name or Optional.empty if either name is null or name is not valid
     *
     */
    public static Optional<CarType> getCarTypeByName(String name) {

        return Optional.ofNullable(map.get(name));
    }

}
