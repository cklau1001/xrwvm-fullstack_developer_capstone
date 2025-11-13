package io.cklau1001.capstone.model;

import jakarta.persistence.*;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;

import lombok.Setter;
import lombok.extern.slf4j.Slf4j;


import java.util.ArrayList;
import java.util.List;

/**
 *
 * Map to APP_USER table
 */
@Slf4j
@Entity
@Getter
@Setter
@NoArgsConstructor
@Table(name = "APP_USER")
public class AppUser extends BasedEntity {

    @Id
    @Column(length = 56)
    private String username;

    @Column(length = 32)
    private String password;

    @Column(length = 12)
    private String firstname;

    @Column(length = 12)
    private String lastname;

    @Column(length = 24)
    private String email;

    /*
       appUser is the property of appRoles that forms the foreign key.
     */
    @OneToMany(mappedBy = "appUser", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<AppRole> appRoles = new ArrayList<>();

    public void addRole(AppRole appRole) {

        log.info("[addRole]: role added to user, role=[{}], user=[{}] entered:", appRole.getRole(), username);

        /*
        We need to create the compository primray keys first by calling the constructor.
         */
        AppRoleId appRoleId = new AppRoleId();
        appRoleId.setUsername(username);
        appRoleId.setRole(appRole.getRole());
        appRole.setAppRoleId(appRoleId);

        appRoles.add(appRole);

        appRole.setAppUser(this);

        log.info("[addRole]: role added to user, role=[{}], user=[{}] end:", appRole.getRole(), username);
    }

    @Override
    public String toString() {

        return "[AppUser]: username=" + username;
    }

}
