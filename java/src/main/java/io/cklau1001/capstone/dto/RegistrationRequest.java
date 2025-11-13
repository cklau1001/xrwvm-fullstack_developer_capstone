package io.cklau1001.capstone.dto;

import io.cklau1001.capstone.model.AppRole;
import io.cklau1001.capstone.model.AppUser;
import jakarta.validation.constraints.Email;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import java.util.List;

@Slf4j
@Data
@AllArgsConstructor
@NoArgsConstructor
public class RegistrationRequest {

    private String userName;
    private String password;
    private String firstName;
    private String lastName;
    @Email
    private String email;

    private String role;

    public AppUser createAppUser() {

        AppUser appUser = new AppUser();
        appUser.setUsername(userName);
        appUser.setPassword(password);
        appUser.setFirstname(firstName);
        appUser.setLastname(lastName);
        appUser.setEmail(email);

        String new_role = role != null ? role : "USER";
        AppRole appRole = new AppRole();
        appRole.setRole(new_role);
        appUser.addRole(appRole);

        log.info("[createAppUser]: Generated appUser={}", appUser);
        return appUser;
    }

    @Override
    public String toString() {

        return "[RegistrationRequest]: username=" + userName + ", role=" + role;
    }

}
