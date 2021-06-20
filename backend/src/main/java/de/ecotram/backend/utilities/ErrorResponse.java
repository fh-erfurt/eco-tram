package de.ecotram.backend.utilities;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
public class ErrorResponse {
    @Getter
    @JsonProperty("error")
    private final String identifier;

    @Getter
    private final String message;
}

