package io.cklau1001.capstone.controller;

import io.cklau1001.capstone.dto.*;
import io.cklau1001.capstone.service.CarService;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.util.List;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/carinfo")
public class CarInfoController {

    private final CarService carService;

    @SecurityRequirement(name = "bearerAuth")
    @PreAuthorize("hasAuthority('ADMIN')")
    @PostMapping("/protected/populate")
    public ResponseEntity<String> populateData() {

        carService.populateData();

        return ResponseEntity.ok("Car data populated");
    }

    /**
     * To create a car maker record
     *
     * @return CarMakerResponse
     */
    @SecurityRequirement(name = "bearerAuth")
    @PreAuthorize("hasAuthority('ADMIN')")
    @PostMapping("/protected/carmaker")
    public ResponseEntity<CarMakerResponse> createCarMaker(@RequestBody CarMakerRequest carMakerRequest) {

        CarMakerResponse carMakerResponse = carService.createCarMaker(carMakerRequest);
        // return ResponseEntity.status(HttpStatus.CREATED).body(carMakerResponse);
        return ResponseEntity.created(URI.create("/carmaker/id/" + carMakerResponse.getMakerId()))
                .body(carMakerResponse);
    }

    /**
     * Create a car model record
     *
     * @param carModelRequest
     * @return carModelResponse DTO object
     */
    @SecurityRequirement(name = "bearerAuth")
    @PreAuthorize("hasAuthority('ADMIN')")
    @PostMapping("/protected/carmodel")
    public ResponseEntity<CarModelResponse> createCarModel(@RequestBody CarModelRequest carModelRequest) {

        CarModelResponse carModelResponse = carService.createCarModel(carModelRequest);
        return ResponseEntity.created(URI.create("/carmodel/id/" + carModelResponse.getModelId()))
                .body(carModelResponse);
    }

    /**
     * Delete a car maker record by the given maker ID
     *
     * @param makerId
     * @return a message of the delete operation.
     */
    @SecurityRequirement(name = "bearerAuth")
    @PreAuthorize("hasAuthority('ADMIN')")
    @DeleteMapping("/protected/carmaker/id/{makerId}")
    public ResponseEntity<String> deleteCarMakerById(@PathVariable("makerId") String makerId) {

        carService.deleteCarMakerById(makerId);
        String message = String.format("Car maker is deleted, makerId=%s", makerId);

        return ResponseEntity.status(HttpStatus.NO_CONTENT).body(message);
    }

    /**
     * Delete a car model record by model ID
     *
     * @param modelId
     * @return a message of the status of the delete operation
     */
    @SecurityRequirement(name = "bearerAuth")
    @PreAuthorize("hasAuthority('ADMIN')")
    @DeleteMapping("/protected/carmodel/id/{modelId}")
    public ResponseEntity<String> deleteCarModelById(@PathVariable("modelId") String modelId) {

        carService.deleteCarModelById(modelId);
        String message = String.format("Car model is deleted, modelId=%s", modelId);

        return ResponseEntity.status(HttpStatus.NO_CONTENT).body(message);

    }

    /*
      Read-only view defined below
     */

    @GetMapping("/carmarkers")
    public ResponseEntity<List<CarMakerResponse>> getAllCarMakers() {

        return ResponseEntity.ok(carService.getAllCarMakers());
    }

    /**
     * Get the car maker details by a maker ID
     *
     * @param makerId
     * @return the car maker detail
     */
    @GetMapping("/carmaker/id/{makerId}")
    public ResponseEntity<CarMakerResponse> getCarMakerById(@PathVariable("makerId") String makerId) {

        return ResponseEntity.ok(carService.getCarMakerResponseById(makerId));
    }

    /**
     * Get all car maker and model names, which is integrated into car review page of React side.
     *
     * @return
     */
    @GetMapping("/cars")
    public ResponseEntity<CarModelsResponse> getAllCarModelMaker() {

        return ResponseEntity.ok(carService.getAllCarModelMaker());
    }

    /**
     * Get the car model record by a given model ID
     *
     * @param modelId
     * @return the car model record
     */
    @GetMapping("/carmodel/id/{modelId}")
    public ResponseEntity<CarModelResponse> getCarModelById(@PathVariable("modelId") String modelId) {

        return ResponseEntity.ok(carService.getCarModelResponseById(modelId));
    }
}
