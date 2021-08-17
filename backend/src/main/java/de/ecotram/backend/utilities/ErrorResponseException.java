package de.ecotram.backend.utilities;

import lombok.Getter;

@Getter
public final class ErrorResponseException extends Exception {
	private final ErrorResponse errorResponse;

	public ErrorResponseException(String identifier, String message) {
		this.errorResponse = new ErrorResponse(identifier, message);
	}
}