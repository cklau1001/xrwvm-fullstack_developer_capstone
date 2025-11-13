package io.cklau1001.capstone.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;

import java.util.List;

@Slf4j
@Getter
@Setter
@NoArgsConstructor
public class DealersResponse {

    public List<DealerResponse> dealers;

    @Override
    public String toString() {

        StringBuffer sb = new StringBuffer();

        dealers.forEach(d -> {sb.append(d).append("\n"); });
        return sb.toString();

    }
}
