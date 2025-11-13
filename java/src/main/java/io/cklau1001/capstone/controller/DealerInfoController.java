package io.cklau1001.capstone.controller;

import io.cklau1001.capstone.dto.*;
import io.cklau1001.capstone.service.DealerService;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/dealerinfo")
public class DealerInfoController {

    final DealerService dealerService;

    @SecurityRequirement(name = "bearerAuth")
    @PreAuthorize("hasAnyAuthority('ADMIN', 'USER')")
    @PostMapping("/protected/review")
    public ResponseEntity<DealerReviewResponse> addDealerReview(@RequestBody DealerReviewRequest dealerReviewRequest) {

        DealerReviewResponse dealerReviewResponse = dealerService.addDealerReview(dealerReviewRequest);
        // return status code 200 to work with PostReview React component
        return ResponseEntity.ok(dealerReviewResponse);
    }

    /**
     * Get a list of information of each dealer
     *
     * @param state
     * @return a list of dealer information
     */
    @GetMapping(value = {"/dealers/**", "/dealers/{state}"})
    public ResponseEntity<DealersResponse> getDealers(@PathVariable(name = "state", required = false) String state) {

        DealersResponse dealersResponse = state == null || state.isEmpty() ?
                dealerService.getAllDealers() : dealerService.getDealers(state);

        return ResponseEntity.ok(dealersResponse);
    }

    /**
     * Get a list of review of a given dealer
     *
     * @param dealership
     * @return a list of review of a given dealer
     */
    @GetMapping("/reviews/dealer/{dealership}")
    public ResponseEntity<DealersReviewResponse> getDealerReviewById(@PathVariable("dealership")  int dealership) {

        DealersReviewResponse dealersReviewResponse = dealerService.getDealersReview(dealership);

        /*
         TODO Need to fill in the sentiment after completing the integration with an external AI service.

         */
        return ResponseEntity.ok(dealersReviewResponse);
    }

    @GetMapping("/dealer/{dealership}")
    public ResponseEntity<DealerResponse> getDealerById(@PathVariable("dealership") int dealership) {

        DealerResponse dealerResponse = dealerService.getDealer(dealership);
        return ResponseEntity.ok(dealerResponse);

    }

}
