package io.cklau1001.capstone.dto;

import io.cklau1001.capstone.model.CarMaker;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;

import java.util.UUID;

/**
  This DTO is used to create new CarMaker record.

 */
@Slf4j
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class CarMakerRequest {

    private String makerName;
    private String makerDescription;

    /**
     * To generate the CarMaker entity from this DTO request object
     *
     * @return CarMaker entity to be saved in database
     */
    public CarMaker generateCarMaker() {

        CarMaker carMaker = CarMaker.getInstance(UUID.randomUUID().toString());
        carMaker.setMakerName(makerName);
        carMaker.setMakerDesciption(makerDescription);

        return carMaker;
    }
}
