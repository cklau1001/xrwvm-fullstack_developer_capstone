package io.cklau1001.capstone.service;

import io.cklau1001.capstone.config.DealerProperties;
import io.cklau1001.capstone.dto.*;
import io.cklau1001.capstone.exception.AppErrorResponse;
import io.cklau1001.capstone.security.JwtUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClient;

import java.util.List;
import java.util.NoSuchElementException;

@Slf4j
@Service
public class DealerService {


    private final DealerProperties dealerProperties;

    private final RestClient restClient;
    private final JwtUtil jwtUtil;

    /**
     * Create a builder service. Need to pass RestClient.Builder as a parameter for RestClientTest to work.
     *
     * @param restClientBuilder - injected RestClient builder
     */
    public DealerService(RestClient.Builder restClientBuilder, JwtUtil JwtUtil, DealerProperties newDealerProperties) {
        this.dealerProperties = newDealerProperties;

        String baseUrl = dealerProperties.getBaseUrl();
        log.info("[DealerService]: baseUrl={}", baseUrl);
        restClient = restClientBuilder.baseUrl(baseUrl).build();
        jwtUtil = JwtUtil;
    }

    /**
     * Get all dealers
     *
     * @return a list of all dealers
     */
    @Cacheable(value = "dealers", key = "#root.methodName") // To work around proxy rule, cache key is method name
    public DealersResponse getAllDealers() {
        log.info("[getAllDealers]: entered");
        return getDealers("");
    }

    /**
     * Get a list of dealers by state and map it to a list of DealerResponse DTO object
     *
     * @param state
     * @return
     */
    @Cacheable(value = "dealers", key = "#state")
    public DealersResponse getDealers(String state) {

        /*
           Map the JSON response list into DealerResponse by ParameterizedTypeReference.
         */
        log.info("[getDealers]: state={}", state);
        List<DealerResponse> dealerResponseList = restClient.get()
                .uri("/fetchDealers/" + state)
                .header("Authorization", generateToken())
                .retrieve()
                .body(new ParameterizedTypeReference<List<DealerResponse>>() {});

        DealersResponse dealersResponse = new DealersResponse();
        dealersResponse.setDealers(dealerResponseList);
        return dealersResponse;
    }

    /**
     * Get a list of review for a given dealer represented by the dealership field (i.e. dealer id)
     *
     * @param dealership
     * @return a list of reviews for the given dealer
     */
    @Cacheable(value = "reviews", key="'reviewed_dealer_id=' + #dealership")
    public DealersReviewResponse getDealersReview(int dealership) {

        List<DealerReviewResponse> dealerReviewResponseList = restClient.get()
                .uri("/fetchReviews/dealer/" + dealership)
                .header("Authorization", generateToken())
                .retrieve()
                .body(new ParameterizedTypeReference<List<DealerReviewResponse>>() {});

        /*
           TODO: Need to get sentiment from external AI
         */
        DealersReviewResponse dealersReviewResponse = new DealersReviewResponse();
        dealersReviewResponse.setReviews(dealerReviewResponseList);
        return dealersReviewResponse;
    }

    /**
     * Get the information of a given dealer
     *
     * @param dealership
     * @return the information of a dealer
     */
    @Cacheable(value = "dealers", key = "'dealer_id=' + #dealership")
    public DealerResponse getDealer(int dealership) {

        /*
        Since the MS returns a list of one element, need to receive the response from a list first
         */
        List<DealerResponse> dealerResponseList = restClient.get()
                .uri("/fetchDealer/" + dealership)
                .header("Authorization", generateToken())
                .retrieve()
                .body(new ParameterizedTypeReference<List<DealerResponse>>() {});


        if (dealerResponseList == null || dealerResponseList.isEmpty()) {
            throw new NoSuchElementException(AppErrorResponse.ErrorCodeConstant.DEALER_NOT_FOUND.getErrorCodeName() +
                    "||Dealer does not exist, dealership=" + dealership);
        }
        return dealerResponseList.get(0);
    }

    /**
     * Add a new review to a given dealer. The cache has to be refreshed to include the new review
     *
     * @param dealerReviewRequest
     * @return DealerReviewResponse DTO object of the review details
     */
    @CacheEvict(value = "reviews", key="'reviewed_dealer_id=' + #dealerReviewRequest.dealership")
    public DealerReviewResponse addDealerReview(DealerReviewRequest dealerReviewRequest) {

        return restClient.post()
                .uri("/insert_review")
                .header("Authorization", generateToken())
                .body(dealerReviewRequest)
                .retrieve()
                .body(DealerReviewResponse.class);

    }

    /*
      TODO: implement the method to add dealer in microservice

     */

    /**
     * Generate JWT token for remote authentication
     *
     * @return
     */
    private String generateToken() {

        return "Bearer " + jwtUtil.createToken("APP", List.of("READ_DEALER"));
    }
}
