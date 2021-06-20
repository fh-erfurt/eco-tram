package de.ecotram.backend.pagination;

import lombok.Getter;
import lombok.Setter;

import java.util.List;

public class PaginationRequest<T> {

    @Getter
    @Setter
    private List<T> results;

    @Getter
    @Setter
    private boolean moreAvailable;

    @Getter
    @Setter
    private int page, limit, count;

    @Getter
    @Setter
    private long total;

}
