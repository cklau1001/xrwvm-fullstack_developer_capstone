package io.cklau1001.capstone.model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;

/**
 * Entity to store the list of car models manufactured by different car makers
 *
 */
@Slf4j
@Getter
@Setter
@Entity(name = "CAR_MODEL")
public class CarModel extends BasedEntity {

    @Id
    @Column(name = "model_id", length = 36)
    private String modelId;

    @Column(name = "model_name", length = 100)
    private String modelName;

    @Column(name = "model_type", length = 32)
    @Enumerated(EnumType.STRING)   // Store the string value of enum, rather than the ordinal number
    private CarType modelType;

    // To allow for unknown year
    @Column(name = "model_year")
    private Integer modelYear;

    @ManyToOne
    @JoinColumn(name = "makerId")
    private CarMaker carMaker;

    @Override
    public String toString() {

        return "[CarModel]: modelId=" + modelId + ", name=" + modelName + ", type=" + modelType + ", year=" + modelYear;
    }

    /**
     * A helper method to return a new CarModel with the provided model id
     *
     */
    public static CarModel getInstance(String modelId) {

        CarModel carModel = new CarModel();
        carModel.setModelId(modelId);

        return carModel;
    }
}
