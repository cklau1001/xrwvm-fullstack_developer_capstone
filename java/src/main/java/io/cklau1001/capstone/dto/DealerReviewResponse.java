package io.cklau1001.capstone.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Getter
@Setter
@NoArgsConstructor
@JsonIgnoreProperties(ignoreUnknown = true)  // ignore fields like _id or __v from JSON response of external MS
public class DealerReviewResponse {

    private Integer id;
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

    private String sentiment = "positive"; // set as default

    @Override
    public String toString() {

        return "[DealerReviewResponse]: id=" + id + ", name=" + name + ", dealership=" + dealership +
                ", review=" + review + ", purchase=" + purchase + ", purchase_date=" + purchaseDate +
                ", car_make=" + carMaker + ", car_model=" + carModel + ", car_year=" + carYear +
                ", sentiment=" + sentiment;
    }

}
