package de.ecotram.backend.utilities;

import lombok.Getter;

public class ErrorResponseException extends Exception {

    @Getter
    private final ErrorResponse errorResponse;

    public ErrorResponseException(String identifier, String message) {
        this.errorResponse = new ErrorResponse(identifier, message);
    }

}
