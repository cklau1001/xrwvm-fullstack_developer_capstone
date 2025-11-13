package io.cklau1001.capstone.model;

import io.cklau1001.capstone.dto.CarMakerResponse;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;

import java.util.ArrayList;
import java.util.List;


/**
 *
 * Table to store the list of car manufacturers.
 *
 */
@Slf4j
@Getter
@Setter
@Entity(name = "CAR_MAKER")
public class CarMaker extends BasedEntity {

    @Id
    @Column(name = "maker_id", length = 36)
    private String makerId;

    @Column(name = "maker_name", length = 36)
    private String makerName;

    @Column(name = "maker_description", length = 128)
    private String makerDesciption;

    @OneToMany(mappedBy = "carMaker", cascade = CascadeType.ALL, orphanRemoval = true)
    List<CarModel> carModelList = new ArrayList<>();

    public void addModel(CarModel carModel) {

        log.info("[addModel]: model=[{}] entered:", carModel);

        /*
        We need to create the compository primray keys first by calling the constructor.
         */
        carModelList.add(carModel);
        carModel.setCarMaker(this);

        log.info("[addModel]: model added, model=[{}], makerName=[{}] end:", carModel, this);
    }

    @Override
    public String toString() {

        return "[CarMaker]: makerId = " + makerId + ", name=" + makerName;
    }

    /**
     * a method to return a CarMaker with carMakerId included for saving into CarModel. No query is made.
     *
     */
     public static CarMaker getInstance(String carMakerId) {
         CarMaker carMaker = new CarMaker();
         carMaker.setMakerId(carMakerId);

         return carMaker;
     }

}
