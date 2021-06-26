package de.ecotram.backend.simulation;

import lombok.Getter;

// TODO(erik): a reporter used to safely check simulation status
public final class ProgressReporter {
    @Getter
    private boolean isClosed;

    void close() {
        isClosed = true;
    }
}
