package io.cklau1001.capstone.dto;

import io.cklau1001.capstone.model.CarMaker;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * This DTO is used to provide detail of a CarMaker
 *
 */
@Slf4j
@Getter
@Setter
@NoArgsConstructor
public class CarMakerResponse {

    public static String MAKERID = "MAKERID";
    public static String MAKERNAME = "MAKERNAME";
    public static String MAKERDESC = "MAKERDESC";
    public static String MAKERCREATEDDATE = "MAKERCREATEDDATE";
    public static String MAKERMODIFIEDDATE = "MAKERMODIFIEDDATE";

    private String makerId;
    private String makerName;
    private String makerDescription;
    private Date createdDate;
    private Date lastModifiedDate;

    private List<CarModelResponse> carModelResponseList = new ArrayList<>();

    /**
     * Create a CarMakerResponse DTO used by Constructor-projection query
     *
     * @param newId
     * @param newName
     * @param newDesc
     * @param newCreatedDate
     * @param newlastModifiedDate
     */
    public CarMakerResponse(String newId, String newName, String newDesc,
                           Date newCreatedDate, Date newlastModifiedDate) {
        this.makerId = newId;
        this.makerName = newName;
        this.makerDescription = newDesc;
        this.createdDate = newCreatedDate;
        this.lastModifiedDate = newlastModifiedDate;
    }

    /**
     * Create a CarMakerResponse DTO used by Map-based projection query
     *
     * @param map
     */
    public CarMakerResponse(Map<String, Object> map) {

        this.makerId = (String) map.get(MAKERID);
        this.makerName = (String) map.get(MAKERNAME);
        this.makerDescription = (String) map.get(MAKERDESC);
        this.createdDate = (Date) map.get(MAKERCREATEDDATE);
        this.lastModifiedDate = (Date) map.get(MAKERMODIFIEDDATE);
    }

    /**
     * Create a new CarMakerResponse by the makerId provided
     *
     * @param makerId
     * @return a new CarMakerResponse
     */
    public static CarMakerResponse getInstance(String makerId) {
        CarMakerResponse carMakerResponse = new CarMakerResponse();
        carMakerResponse.setMakerId(makerId);

        return carMakerResponse;

    }
}
