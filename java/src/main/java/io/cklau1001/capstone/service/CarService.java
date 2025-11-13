package io.cklau1001.capstone.service;

import io.cklau1001.capstone.dto.*;
import io.cklau1001.capstone.exception.AppErrorResponse;
import io.cklau1001.capstone.model.CarMaker;
import io.cklau1001.capstone.model.CarModel;
import io.cklau1001.capstone.model.CarType;
import io.cklau1001.capstone.repository.CarMakerRepository;
import io.cklau1001.capstone.repository.CarModelRepository;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.*;

/**
 * This is the Car service that plays as a link between controller and backend CarMaker and CarModel repositories.
 * It is responsible for converting Entity and DTO objects as appropriate.
 *
 */
@Slf4j
@RequiredArgsConstructor
@Service
public class CarService {

    private final CarMakerRepository carMakerRepository;
    private final CarModelRepository carModelRepository;

    public void populateData() {

        List<Map<String, String>> carMakerData = List.of(
                Map.of("id", UUID.randomUUID().toString(), "name", "NISSAN", "description", "Great Cars. Japanese Technology"),
                Map.of("id", UUID.randomUUID().toString(), "name", "Mercedes", "description", "Great Cars. German Technology"),
                Map.of("id", UUID.randomUUID().toString(), "name", "Audi", "description", "Great Cars. Japanese Technology"),
                Map.of("id", UUID.randomUUID().toString(), "name", "Kia", "description", "Great Cars. Korean Technology"),
                Map.of("id", UUID.randomUUID().toString(), "name", "Toyota", "description", "Great Cars. Japanese Technology")
        );

        List<CarMaker> carMakerList = new ArrayList<>();

        List<Map<String, Object>> carModelData = List.of(
                Map.of("id", UUID.randomUUID().toString(), "name", "Pathfinder", "type", "SUV", "year", 2023, "maker", 0),
                Map.of("id", UUID.randomUUID().toString(), "name", "Qashqai", "type", "SUV", "year", 2023, "maker", 0),
                Map.of("id", UUID.randomUUID().toString(), "name", "XTRAIL", "type", "SUV", "year", 2023, "maker", 0),
                Map.of("id", UUID.randomUUID().toString(), "name", "A-Class", "type", "SUV", "year", 2023, "maker", 1),
                Map.of("id", UUID.randomUUID().toString(), "name", "C-Class", "type", "SUV", "year", 2023, "maker", 1),
                Map.of("id", UUID.randomUUID().toString(), "name", "E-Class", "type", "SUV", "year", 2023, "maker", 1),
                Map.of("id", UUID.randomUUID().toString(), "name", "A4", "type", "SUV", "year", 2023, "maker", 2),
                Map.of("id", UUID.randomUUID().toString(), "name", "A5", "type", "SUV", "year", 2023, "maker", 2),
                Map.of("id", UUID.randomUUID().toString(), "name", "A6", "type", "SUV", "year", 2023, "maker", 2),
                Map.of("id", UUID.randomUUID().toString(), "name", "Sorrento", "type", "SUV", "year", 2023, "maker", 3),
                Map.of("id", UUID.randomUUID().toString(), "name", "Carnival", "type", "SUV", "year", 2023, "maker", 3),
                Map.of("id", UUID.randomUUID().toString(), "name", "Cerato", "type", "Sedan", "year", 2023, "maker", 3),
                Map.of("id", UUID.randomUUID().toString(), "name", "Corolla", "type", "Sedan", "year", 2023, "maker", 4),
                Map.of("id", UUID.randomUUID().toString(), "name", "Camry", "type", "Sedan", "year", 2023, "maker", 4),
                Map.of("id", UUID.randomUUID().toString(), "name", "Kluger", "type", "SUV", "year", 2023, "maker", 4)
        );

        // Remove all data first
        log.debug("[populateData]: BEGIN Plunged all data");
        carModelRepository.deleteAll();
        carMakerRepository.deleteAll();

        log.debug("[populateData]: AFTER Plunged all data, BEGIN loading carMaker data ");
        for (Map<String, String> carMakerRecord: carMakerData) {
            CarMaker carMaker = new CarMaker();
            carMaker.setMakerId(carMakerRecord.get("id"));
            carMaker.setMakerName(carMakerRecord.get("name"));
            carMaker.setMakerDesciption(carMakerRecord.get("description"));
            carMakerRepository.save(carMaker);
            carMakerList.add(carMaker);
        }

        log.debug("[populateData]: AFTER loading carMaker data, BEGIN loading carModel data ");
        int count = 0;
        for (Map<String, Object> carModelRecord: carModelData) {
            String modelName = (String) carModelRecord.get("name");
            String modelType = (String) carModelRecord.get("type");

            log.debug("[populateData]: [{}] modelName={}, modelType={}", count, modelName, modelType);
            CarModel carModel = new CarModel();
            carModel.setModelId((String) carModelRecord.get("id"));
            carModel.setModelName((String) carModelRecord.get("name"));
            carModel.setModelType(CarType.getCarTypeByName((String) carModelRecord.get("type"))
                    .orElseThrow(() -> new NoSuchElementException(
                            AppErrorResponse.ErrorCodeConstant.UNKNOWN_ERROR.getErrorCodeName() +
                                    "||Invalid CarType=" + carModelRecord.get("type")))
            );

            carModel.setModelYear((Integer) carModelRecord.get("year"));

            carModel.setCarMaker(carMakerList.get((int) carModelRecord.get("maker")));
            carModelRepository.save(carModel);
            count++;
        }

        log.debug("[populateData]: Done");
    }

    /**
     * Create a car maker record
     *
     * @return a CarMakerResponse for CarInfoController to return to client
     */
    public CarMakerResponse createCarMaker(CarMakerRequest carMakerRequest) {

        CarMaker carMaker = carMakerRequest.generateCarMaker();
        carMakerRepository.save(carMaker);

        CarMakerResponse carMakerResponse = CarMakerResponse.getInstance(carMaker.getMakerId());
        carMakerResponse.setMakerId(carMaker.getMakerId());
        carMakerResponse.setMakerDescription(carMaker.getMakerDesciption());
        carMakerResponse.setMakerName(carMaker.getMakerName());

        return carMakerResponse;
    }

    /**
     * Create a Car model record in database from carModelRequest DTO
     *
     *
     * @param carModelRequest*
     * @return a CarModelResponse DTO with the model ID included
     */
    public CarModelResponse createCarModel(CarModelRequest carModelRequest) {

        CarModel carModel = carModelRequest.generateCarModel();
        carModelRepository.save(carModel);

        CarModelResponse carModelResponse = CarModelResponse.getInstance(carModel.getModelId());
        carModelResponse.setModelType(carModel.getModelType());
        carModelResponse.setModelYear(carModel.getModelYear());
        carModelResponse.setModelName(carModel.getModelName());
        carModelResponse.setMakerId(carModel.getCarMaker().getMakerId());
        return carModelResponse;

    }

    /**
     * Delete a car maker by a maker ID
     *
     * @param makerId
     * @return true if makeId is valid, or false if it has been deleted
     */
    public void deleteCarMakerById(String makerId) {

        carMakerRepository.deleteById(makerId);
        log.info("[deleteCarMakerById]: maker is deleted, makerId={}", makerId);
    }

    public void deleteCarModelById(String modelId) {

        carModelRepository.deleteById(modelId);
        log.info("[deleteCarModelById]: model is deleted, makerId={}", modelId);
    }

    /*
      Read-only queries defined below

     */

    public CarMakerResponse getCarMakerResponseById(String makerId) {

        return carMakerRepository.findByMakerIdAsDTO(makerId).orElseThrow(
                () -> new EntityNotFoundException(
                        AppErrorResponse.ErrorCodeConstant.CAR_MAKER_NOT_FOUND.getErrorCodeName() +
                                "||Car maker does not exist, makerId=" + makerId
                )
        );
    }

    /**
     * Get all car makers from database and return the result as CarMakerResponse
     *
     * @return
     */
    public List<CarMakerResponse> getAllCarMakers() {

        List<Map<String, Object>> resultset = carMakerRepository.findAllAsDTOMap();
        return resultset.stream().map(CarMakerResponse::new).toList();
    }

    public CarModelsResponse getAllCarModelMaker() {

        List<Map<String, String>> resultset = carModelRepository.findModelNameMakerName();
        CarModelsResponse carModelsResponse = new CarModelsResponse();

        for (Map<String, String> record: resultset) {
            carModelsResponse.getCarModels().add(new CarInfoPreviewResponse(
                    record.getOrDefault("CARMODEL", ""),
                    record.getOrDefault("CARMAKE", "")));
        }

        return carModelsResponse;
    }

    public CarModelResponse getCarModelResponseById(String modelId) {

        return carModelRepository.findByModelIdAsDTO(modelId).orElseThrow(
                () -> new EntityNotFoundException(
                        AppErrorResponse.ErrorCodeConstant.CAR_MODEL_NOT_FOUND.getErrorCodeName() +
                                "||Car model does not exist, modelId=" + modelId
                )
        );
    }

    /**
     * Return a list of Car models from database
     *
     * @return a list of car models
     */
    public List<CarModelResponse> getAllCarModels() {

        List<Map<String, Object>> resultset = carModelRepository.findAllAsDTOMap();
        return resultset.stream().map(CarModelResponse::new).toList();

    }

}
