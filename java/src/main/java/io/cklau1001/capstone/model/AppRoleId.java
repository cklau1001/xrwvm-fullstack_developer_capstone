package io.cklau1001.capstone.model;

import jakarta.persistence.Embeddable;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import java.io.Serializable;
import java.util.Objects;

@Slf4j
@Data
@Embeddable
@NoArgsConstructor
public class AppRoleId implements Serializable {

    private String role;
    private String username;

    @Override
    public boolean equals(Object o) {

        if (this == o) return true;
        if (!(o instanceof AppRoleId that)) return false;
        return Objects.equals(role, that.role) && Objects.equals(username, that.username);

    }

    @Override
    public int hashCode() {
        return Objects.hash(role, username);
    }
}
