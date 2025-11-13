package io.cklau1001.capstone.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * DTO for Login request. To allow Spring convert POST payload into DTO objects, please ensure
 *  1-empty constructor is set up
 *  2-setter and getter are provided.
 *  3-RequestBody from Spring web, not from JsonWeb
 *
 */
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class LoginRequest {

    private String userName;
    private String password;

}
