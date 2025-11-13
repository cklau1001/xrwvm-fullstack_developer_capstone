package io.cklau1001.capstone.repository;

import io.cklau1001.capstone.dto.CarModelResponse;
import io.cklau1001.capstone.model.CarModel;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Map;
import java.util.Optional;

@Repository
public interface CarModelRepository extends JpaRepository<CarModel, String> {


    @Query("SELECT cmo.modelName as CARMODEL, cma.makerName as CARMAKE FROM CAR_MODEL cmo JOIN CAR_MAKER cma ON cmo.carMaker.makerId = cma.makerId")
    // @Query("SELECT cmo.modelName, cmo.modelId from CAR_MODEL cmo" )
    public List<Map<String, String>> findModelNameMakerName();

    /*
        public CarModelResponse(String newModelId, String newModelType, String newModelName,
                            Integer newModelYear, String newMakerId,
                            Date newCreatedDate, Date newlastModifiedDate) {

     */
    @Query("SELECT cmo.modelId AS MODELID, cmo.modelType AS MODELTYPE, cmo.modelName AS MODELNAME, " +
            "cmo.modelYear AS MODELYEAR, cmo.carMaker.makerId AS MAKERID, " +
            "cmo.createdDate as MODELCREATEDDATE, cmo.lastModifiedDate AS MODELMODIFIEDDATE " +
            " FROM CAR_MODEL cmo")
    public List<Map<String, Object>> findAllAsDTOMap();

    @Query("SELECT new io.cklau1001.capstone.dto.CarModelResponse(" +
            "cmo.modelId, cmo.modelType, cmo.modelName, " +
            "cmo.modelYear, cmo.carMaker.makerId, " +
            "cmo.createdDate, cmo.lastModifiedDate) " +
            "FROM CAR_MODEL cmo WHERE cmo.modelId = :modelId")
    public Optional<CarModelResponse> findByModelIdAsDTO(@Param("modelId") String modelId);

}
