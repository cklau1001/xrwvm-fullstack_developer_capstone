package io.cklau1001.capstone.exception;

import com.fasterxml.jackson.annotation.JsonValue;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;

import java.util.HashMap;
import java.util.Map;

@Slf4j
@RequiredArgsConstructor
@Getter
@Setter
public class AppErrorResponse {

    @RequiredArgsConstructor
    @Getter
    public enum ErrorCodeConstant {

        INVALID_CREDENTIALS("INVALID_CREDENTIALS", "USER", HttpStatus.UNAUTHORIZED , "The provided credentials are invalid."),
        USER_NOT_FOUND("USER_NOT_FOUND", "USER", HttpStatus.UNAUTHORIZED , "The specified user does not exist."),
        USER_NOT_AUTHORIZED("USER_NOT_AUTHORIZED", "USER", HttpStatus.UNAUTHORIZED , "The specified user is not authorized to perform the action."),
        INTERNAL_SERVER_ERROR("INTERNAL_SERVER_ERROR", "SYSTEM", HttpStatus.INTERNAL_SERVER_ERROR , "An unexpected error occurred on the server."),
        UNKNOWN_CAR_TYPE("UNKNOWN_CART_TYPE", "USER", HttpStatus.NOT_FOUND, "The provided Car Type is not found."),
        CAR_MAKER_NOT_FOUND("CAR_MAKER_NOT_FOUND", "USER", HttpStatus.NOT_FOUND , "The specified car maker does not exist."),
        CAR_MODEL_NOT_FOUND("CAR_MODEL_NOT_FOUND", "USER", HttpStatus.NOT_FOUND , "The specified car model does not exist."),
        DEALER_NOT_FOUND("DEALER_NOT_FOUND", "USER", HttpStatus.NOT_FOUND , "The specified dealer does not exist."),
        HTTP_CLIENT_ERROR("HTTP_CLIENT_ERROR", "SYSTEM", HttpStatus.INTERNAL_SERVER_ERROR, "There is an issue when invoking the external microservice"),
        UNKNOWN_ERROR("UNKNOWN_ERROR", "USER", HttpStatus.INTERNAL_SERVER_ERROR , "Unknown error.");

        final private String errorCodeName;
        final private String errorCategory;
        final private HttpStatus errorStatusCode;
        final private String errorDescription;

        private static final Map<String, ErrorCodeConstant> lookup = new HashMap<>();

        static {

            for (ErrorCodeConstant errorCodeConstant : ErrorCodeConstant.values()) {

                lookup.put(errorCodeConstant.getErrorCodeName(), errorCodeConstant);
            }
        }

        public static ErrorCodeConstant getByErrorCodeName(String errorCodeName) {
            return lookup.getOrDefault(errorCodeName, UNKNOWN_ERROR);
        }

        /*
          @JsonValue is the hook to customize the output Json structure. A Map is used in this case.
         */
        @JsonValue
        public Map<String, Object> toJson() {

            return Map.of("errorCodeName", errorCodeName,
                          "errorCategory", errorCategory,
                          "errorStatusCode", errorStatusCode,
                          "errorDescription", errorDescription
                    );
        }

    } // enum ErrorCodeConstant

    private ErrorCodeConstant errorCodeConstant;
    private String errorMessage;
    private String exceptionClass;

    public AppErrorResponse(ErrorCodeConstant newErrorCodeConstant,
                            String newErrorMessage,
                            String newExceptionClassName) {
        this.errorCodeConstant = newErrorCodeConstant;
        this.errorMessage = newErrorMessage;
        this.exceptionClass = newExceptionClassName;
    }

    @Override
    public String toString() {

        return "[appErrorResponse]: errorCode=" + errorCodeConstant.errorCodeName +
                ", errorMessage=" + errorMessage + ", exceptionClass=" + exceptionClass;
    }


}
