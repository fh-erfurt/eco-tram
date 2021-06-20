package de.ecotram.backend.pagination;

import lombok.Data;

import java.util.List;

@Data
public final class PaginationRequest<T> {
    private List<T> results;
    private boolean moreAvailable;
    private int page, limit, count;
    private long total;
}