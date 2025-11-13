package io.cklau1001.capstone.exception;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.security.core.Authentication;
import org.springframework.web.client.RestClientException;

@Slf4j
@ControllerAdvice
public class GlobalExceptionHandler {

    private void logError(AppErrorResponse appErrorResponse) {
        log.error("[logError]: " + appErrorResponse.toString());
    }

    @ExceptionHandler(AccessDeniedException.class)
    public ResponseEntity<AppErrorResponse> handleAccessDeniedException(AccessDeniedException ex) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String user = authentication != null ? authentication.getName() : "Unknown user";

        String errorMessage = ex.getMessage() + ", user=" + user;
        AppErrorResponse appErrorResponse = new AppErrorResponse(
                AppErrorResponse.ErrorCodeConstant.USER_NOT_AUTHORIZED, errorMessage, ex.getClass().getName()
        );
        HttpStatus status = appErrorResponse.getErrorCodeConstant().getErrorStatusCode();
        logError(appErrorResponse);
        return ResponseEntity.status(status).body(appErrorResponse);

    }

    /**
     * Handle the exception caught when triggering endpoints from external microservices
     *
     * @param re
     * @return
     */
    @ExceptionHandler(RestClientException.class)
    public ResponseEntity<AppErrorResponse> handleHttpClientException(RestClientException re) {

        AppErrorResponse appErrorResponse = new AppErrorResponse(
                AppErrorResponse.ErrorCodeConstant.HTTP_CLIENT_ERROR, re.getMessage(), re.getClass().getName()
        );
        HttpStatus status = appErrorResponse.getErrorCodeConstant().getErrorStatusCode();
        logError(appErrorResponse);
        return ResponseEntity.status(status).body(appErrorResponse);

    }

    /**
     * Convert an error message into an AppErrorResponse with the following syntax
     *    errorMessage =:  <error-code> || <error-message>
     *
     * @param ex
     * @return
     */
    @ExceptionHandler(Exception.class)
    public ResponseEntity<AppErrorResponse> handleAllExceptions(Exception ex) {
        String errorMessage = ex.getMessage();
        String[] parts = errorMessage != null ? errorMessage.split("\\|\\|", 2) : new String[0];
        AppErrorResponse.ErrorCodeConstant errorResponseConstant = AppErrorResponse.ErrorCodeConstant.getByErrorCodeName(parts[0]);
        AppErrorResponse appErrorResponse = new AppErrorResponse(errorResponseConstant,
                parts.length > 1 ? parts[1] : errorMessage,
                ex.getClass().getName());

        HttpStatus status = appErrorResponse.getErrorCodeConstant().getErrorStatusCode();
        logError(appErrorResponse);
        return ResponseEntity.status(status).body(appErrorResponse);
    }



}
