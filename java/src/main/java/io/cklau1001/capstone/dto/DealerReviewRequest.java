package io.cklau1001.capstone.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Getter
@Setter
@NoArgsConstructor
public class DealerReviewRequest {

    private String name;
    private Integer dealership;
    private String review;
    private boolean purchase;
    @JsonProperty("purchase_date")
    private String purchaseDate;

    @JsonProperty("car_make")
    private String carMaker;

    @JsonProperty("car_model")
    private String carModel;

    @JsonProperty("car_year")
    private Integer carYear;

    @Override
    public String toString() {

        return "[DealerReviewRequest]: name=" + name + ", dealership=" + dealership +
                ", review=" + review + ", purchase=" + purchase + ", purchase_date=" + purchaseDate +
                ", car_make=" + carMaker + ", car_model=" + carModel + ", car_year=" + carYear;
    }

}
