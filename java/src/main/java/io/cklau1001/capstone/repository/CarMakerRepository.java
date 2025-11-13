package io.cklau1001.capstone.repository;

import io.cklau1001.capstone.dto.CarMakerResponse;
import io.cklau1001.capstone.model.CarMaker;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Map;
import java.util.Optional;

@Repository
public interface CarMakerRepository extends JpaRepository<CarMaker, String> {

    @Query("SELECT makerId AS MAKERID, makerName AS MAKERNAME, makerDesciption AS MAKERDESC, " +
            "createdDate AS MAKERCREATEDDATE, lastModifiedDate as MAKERMODIFIEDDATE from CAR_MAKER")
    public List<Map<String, Object>> findAllAsDTOMap();

    /*
        public CarMakerResponse(String newId, String newName, String newDesc,
                            Date newCreatedDate, Date newlastModifiedDate) {

     */
    @Query("SELECT new io.cklau1001.capstone.dto.CarMakerResponse(" +
            "cma.makerId, cma.makerName, cma.makerDesciption, cma.createdDate, cma.lastModifiedDate) " +
            "FROM CAR_MAKER cma WHERE cma.makerId = :makerId")
    public Optional<CarMakerResponse> findByMakerIdAsDTO(@Param("makerId") String makerId);
}
