package io.cklau1001.capstone.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

/**
 * This DTO is created for the get_cars endpoint of Postreview.jsx.
 *
 */
@NoArgsConstructor
@Getter
@Setter
public class CarModelsResponse {

    @JsonProperty("CarModels")
    private List<CarInfoPreviewResponse> carModels = new ArrayList<>();


}
