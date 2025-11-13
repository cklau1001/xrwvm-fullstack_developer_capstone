package io.cklau1001.capstone.repository;

import io.cklau1001.capstone.model.AppRole;
import io.cklau1001.capstone.model.AppRoleId;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface AppRoleRepository extends JpaRepository<AppRole, AppRoleId> {
    
    List<AppRole> findByAppRoleIdUsername(String username);


}
