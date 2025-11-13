package io.cklau1001.capstone.model;

import jakarta.persistence.*;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;

/**
 * Map to APP_ROLE table
 *
 * @Data causes warning on hashCode() and toString(), use Getter and Setter instead
 */
@Entity
@Getter
@Setter
@Table(name = "APP_ROLE")
public class AppRole extends BasedEntity {

    /*
     To create a composite primary keys (username, role)
     */
    @EmbeddedId
    private AppRoleId appRoleId;

    /*
    To avoid warning of duplicate column name, set one for non-editable
     */
    @Column(insertable = false, updatable = false)
    private String role;

    @ManyToOne
    @MapsId("username")    // map to actual property (username) of appRoleId
    @JoinColumn(name = "username")   // map to username property of appUser
    private AppUser appUser;
}
