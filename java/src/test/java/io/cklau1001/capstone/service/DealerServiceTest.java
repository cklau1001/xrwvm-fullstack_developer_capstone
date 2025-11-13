package io.cklau1001.capstone.service;

import io.cklau1001.capstone.config.DealerProperties;
import io.cklau1001.capstone.config.JwtUtilProperties;
import io.cklau1001.capstone.dto.*;
import io.cklau1001.capstone.security.JwtUtil;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.autoconfigure.web.client.RestClientTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.springframework.test.web.client.MockRestServiceServer;
import org.springframework.web.client.RestClient;

import java.security.SecureRandom;
import java.util.function.Supplier;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.springframework.test.web.client.match.MockRestRequestMatchers.requestTo;
import static org.springframework.test.web.client.response.MockRestResponseCreators.withSuccess;

/*
By default, @RestClientTest only loads beans related to RestClient and the specified service. Need
to include all Beans involved in this test.

 */
@Slf4j
@RestClientTest({DealerService.class, JwtUtil.class, DealerProperties.class, JwtUtilProperties.class})
public class DealerServiceTest {

    private static final Supplier<String> randomStringGenerator = () -> {
        final String characters = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789";
        final int strLen = 43;  // at least 43 bytes to return a 32-byte base64 decoded key
        SecureRandom sr = new SecureRandom();
        return sr.ints(strLen, 0, characters.length())
                .mapToObj(characters::charAt)
                .collect(StringBuffer::new, StringBuffer::append, StringBuffer::append)
                .toString();

    };

    /**
     * Registered required properties for testing below
     * @param registry
     */
    @DynamicPropertySource
    static void configureProperties(DynamicPropertyRegistry registry) {
        registry.add("application.dealer.baseUrl", () -> "http://localhost:3030");
        registry.add("application.jwtutil.secretKey", randomStringGenerator::get);
        registry.add("application.jwtutil.signAlg", () -> "HmacSHA256");

    }

    @Autowired
    private MockRestServiceServer mockRestServiceServer;

    @Value("${application.dealer.baseUrl}")
    private String dealerBaseUrl;

    /*
      Autoware jwtUtilProperties that reads from DynamicPropertyRegistry below
     */
    @Autowired
    private JwtUtilProperties jwtUtilProperties;

    @Autowired
    private DealerService dealerService;

    /**
     * Test external microservice connection
     */
    @Disabled("Enable this test to test the MS connection")
    @Test
    void testDealerMicroserviceConnection() {

        /*
         To test the real connection, need to create a dealerService

         */

        RestClient.Builder b = RestClient.builder();
        JwtUtil jwtUtil = new JwtUtil(jwtUtilProperties);
        DealerProperties dealerProperties = new DealerProperties();
        dealerProperties.setBaseUrl(dealerBaseUrl);
        log.info("baseUrl = {}", dealerBaseUrl);
        DealerService dealerService = new DealerService(b, jwtUtil, dealerProperties);
        DealersResponse dealersResponse = dealerService.getAllDealers();
        log.info("dealersResponse = {}", dealersResponse);

        // DealersReviewResponse dealersReviewResponse = dealerService.getDealersReview(15);
        // log.info("dealersReviewResponse = {}", dealersReviewResponse);

        // DealerResponse dealerResponse = dealerService.getDealer(1);
        // log.info("DealerResponse = {}", dealerResponse);

        DealerReviewRequest dealerReviewRequest = new DealerReviewRequest();
        dealerReviewRequest.setName("Reviewer1");
        dealerReviewRequest.setDealership(15);
        dealerReviewRequest.setReview("This is a good service");
        dealerReviewRequest.setPurchase(true);
        dealerReviewRequest.setPurchaseDate("01/09/2022");
        dealerReviewRequest.setCarMaker("Audi");
        dealerReviewRequest.setCarModel("A6");
        dealerReviewRequest.setCarYear(2010);

        // DealerReviewResponse dealerReviewResponse = dealerService.addDealerReview(dealerReviewRequest);
        // log.info("dealerReviewResponse = {}", dealerReviewResponse);
    }

    @Test
    void getAllDealers() {

        String jsonResponse = """
[
  {
    "_id": "6912958be9148e304a210fde",
    "id": 1,
    "city": "El Paso",
    "state": "Texas",
    "address": "3 Nova Court",
    "zip": "88563",
    "lat": "31.6948",
    "long": "-106.3",
    "short_name": "Holdlamis",
    "full_name": "Holdlamis Car Dealership",
    "__v": 0
  }]
                """;

        mockRestServiceServer.expect(requestTo(dealerBaseUrl + "/fetchDealers/"))
                .andRespond(withSuccess(jsonResponse, MediaType.APPLICATION_JSON));

        DealersResponse dealersResponse = dealerService.getAllDealers();
        assertThat(dealersResponse.getDealers().size()).isEqualTo(1);
        mockRestServiceServer.verify();
    }

    @Test
    void getDealersReviewSuccess() {

        String jsonResponse = """
[
  {
    "_id": "6912958be9148e304a210fab",
    "id": 1,
    "name": "Berkly Shepley",
    "dealership": 15,
    "review": "Total grid-enabled service-desk",
    "purchase": true,
    "purchase_date": "07/11/2020",
    "car_make": "Audi",
    "car_model": "A6",
    "car_year": 2010,
    "__v": 0
  }]
                """;

        mockRestServiceServer.expect(requestTo(dealerBaseUrl + "/fetchReviews/dealer/15"))
                .andRespond(withSuccess(jsonResponse, MediaType.APPLICATION_JSON));

        DealersReviewResponse dealersReviewResponse = dealerService.getDealersReview(15);
        assertThat(dealersReviewResponse.getReviews().size()).isEqualTo(1);
        mockRestServiceServer.verify();

    }

}
