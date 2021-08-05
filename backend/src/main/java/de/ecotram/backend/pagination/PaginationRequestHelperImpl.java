package de.ecotram.backend.pagination;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.util.List;

public final class PaginationRequestHelperImpl<T> implements PaginationRequestHelper<T> {
    @PersistenceContext
    EntityManager entityManager;

    @Override
    public PaginationRequest<T> getAsPaginationRequest(Class<T> tClass, int limit, int page, PaginationRequest<T> paginationRequest) {
        List<T> results = limit > 0 ?
                entityManager
                        .createQuery("select t from " + tClass.getSimpleName() + " t", tClass)
                        .setMaxResults(limit + 1)
                        .setFirstResult(limit * page)
                        .getResultList() :
                entityManager
                        .createQuery("select t from " + tClass.getSimpleName() + " t", tClass)
                        .getResultList();

        long total = (long) entityManager
                .createQuery("select count(*) from " + tClass.getSimpleName() + " t")
                .getSingleResult();

        boolean moreAvailable = limit > 0 && results.size() > limit;

        paginationRequest.setResults(moreAvailable ? results.subList(0, limit) : results);
        paginationRequest.setLimit(limit);
        paginationRequest.setPage(page);
        paginationRequest.setTotal(total);
        paginationRequest.setMoreAvailable(moreAvailable);
        paginationRequest.setCount(paginationRequest.getResults().size());

        return paginationRequest;
    }

    @Override
    public PaginationRequest<T> getAsPaginationRequest(Class<T> tClass, int limit, int page) {
        return getAsPaginationRequest(tClass, limit, page, new PaginationRequest<>());
    }

    @Override
    public PaginationRequest<T> getAsPaginationRequest(Class<T> tClass, int limit) {
        return getAsPaginationRequest(tClass, limit, 0, new PaginationRequest<>());
    }

    @Override
    public PaginationRequest<T> getAsPaginationRequest(Class<T> tClass) {
        return getAsPaginationRequest(tClass, 20, 0, new PaginationRequest<>());
    }

    @Override
    public PaginationRequest<T> getAsPaginationRequest(Class<T> tClass, PaginationRequest<T> paginationRequest) {
        return getAsPaginationRequest(tClass, paginationRequest.getLimit(), paginationRequest.getPage(), paginationRequest);
    }
}