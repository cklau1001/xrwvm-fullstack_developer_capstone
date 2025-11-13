package io.cklau1001.capstone.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;

/**
 * Capture the attributes of a dealer, which is the basis of DealersResponse and OneDealerResponse.
 *
 */
@Slf4j
@Getter
@Setter
@NoArgsConstructor
@JsonIgnoreProperties(ignoreUnknown = true)  // ignore fields like _id or __v from JSON response of external MS
public class DealerResponse {
    private Integer id;
    private String city;
    private String state;
    private String address;
    private String zip;

    @JsonProperty("lat")
    private String latitude;

    @JsonProperty("long")
    private String longitude;

    @JsonProperty("short_name")
    private String shortName;

    @JsonProperty("full_name")
    private String fullName;

    @Override
    public String toString() {

        return "[DealerResponse]: id=" + id + ", city=" + city + ", state=" + state +
                ", address=" + address + ", zip=" + zip + ", lat=" + latitude +
                ", long=" + longitude + ", short_name=" + shortName + ", full_name=" + fullName;
    }
}
