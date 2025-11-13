package io.cklau1001.capstone.dto;

import io.cklau1001.capstone.model.CarMaker;
import io.cklau1001.capstone.model.CarModel;
import io.cklau1001.capstone.model.CarType;
import lombok.*;
import lombok.extern.slf4j.Slf4j;

import java.util.UUID;

/**
 * This DTO is used to create a CarModel record
 *
 */
@Slf4j
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class CarModelRequest {

    private CarType modelType;
    private String modelName;
    private Integer modelYear;
    private String makerId;

    /**
     * To generate the CarMaker entity from this DTO request object
     *
     * @return CarMaker entity to be saved in database
     */
    public CarModel generateCarModel() {

        CarModel carModel = CarModel.getInstance(UUID.randomUUID().toString());
        carModel.setModelYear(modelYear);
        carModel.setModelType(modelType);
        carModel.setModelName(modelName);
        carModel.setCarMaker(CarMaker.getInstance(makerId));

        return carModel;
    }
}
