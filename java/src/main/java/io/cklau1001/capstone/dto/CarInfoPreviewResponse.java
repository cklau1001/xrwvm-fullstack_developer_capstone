package io.cklau1001.capstone.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class CarInfoPreviewResponse {

    @JsonProperty("CarModel")
    private String carModel;

    @JsonProperty("CarMake")
    private String carMake;

}
