package io.cklau1001.capstone.dto;

import io.cklau1001.capstone.model.CarType;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;

import java.util.Date;
import java.util.Map;

/**
 * This DTO is used to show the detail of a car model.
 *
 */
@Slf4j
@NoArgsConstructor
@Getter
@Setter
public class CarModelResponse {

    public static String MODELID = "MODELID";
    public static String MODELTYPE = "MODELTYPE";
    public static String MODELNAME = "MODELNAME";
    public static String MODELYEAR = "MODELYEAR";
    public static String MAKERID = "MAKERID";
    public static String MODELCREATEDDATE = "MODELCREATEDDATE";
    public static String MODELMODIFIEDDATE = "MODELMODIFIEDDATE";

    private String modelId;
    private CarType modelType;
    private String modelName;
    private Integer modelYear;
    private String makerId;
    private Date createdDate;
    private Date lastModifiedDate;

    /**
     * Create a new CarModelResponse DTO to be used by constructor-projection query
     *
     * @param newModelId
     * @param newModelType
     * @param newModelName
     *
     *
     */
    public CarModelResponse(String newModelId, CarType newModelType, String newModelName,
                            Integer newModelYear, String newMakerId,
                            Date newCreatedDate, Date newlastModifiedDate) {
        this.modelId = newModelId;
        this.modelName = newModelName;
        this.modelType = newModelType;
        this.modelYear = newModelYear;
        this.makerId = newMakerId;
        this.createdDate = newCreatedDate;
        this.lastModifiedDate = newlastModifiedDate;
    }

    /**
     * Create a new CarModelResponse DTO to be used by Map-based projection query
     *
     * @param map
     */
    public CarModelResponse(Map<String, Object> map) {
        this.modelId = (String) map.get(MODELID);
        this.modelName = (String) map.get(MODELNAME);
        this.modelType = CarType.getCarTypeByName((String) map.get(MODELTYPE)).orElse(null);
        this.modelYear = (Integer) map.get(MODELYEAR);
        this.makerId = (String) map.get(MAKERID);
        this.createdDate = (Date) map.get(MODELCREATEDDATE);
        this.lastModifiedDate = (Date) map.get(MODELMODIFIEDDATE);
    }

    /**
     * Return a new CarModelResponse by the model ID provided
     *
     * @param modelId
     * @return a new CarModelResponse
     */
    public static CarModelResponse getInstance(String modelId) {
        CarModelResponse carModelResponse = new CarModelResponse();
        carModelResponse.setModelId(modelId);

        return carModelResponse;
    }

}
