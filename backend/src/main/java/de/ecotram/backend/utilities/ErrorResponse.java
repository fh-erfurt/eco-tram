package de.ecotram.backend.utilities;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public final class ErrorResponse {
    @JsonProperty("error")
    private final String identifier;
    private final String message;
}