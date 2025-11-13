package io.cklau1001.capstone.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;

import java.util.List;

@Slf4j
@NoArgsConstructor
@Getter
@Setter
public class DealersReviewResponse {

    private List<DealerReviewResponse> reviews;

    @Override
    public String toString() {

        StringBuffer sb = new StringBuffer();

        reviews.forEach(d -> {sb.append(d).append("\n"); });
        return sb.toString();

    }
}
