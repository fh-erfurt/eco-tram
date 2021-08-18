package de.ecotram.backend.pagination;

public interface PaginationRequestHelper<T> {
	PaginationRequest<T> getAsPaginationRequest(Class<T> tClass, int limit, int page, PaginationRequest<T> paginationRequest);

	PaginationRequest<T> getAsPaginationRequest(Class<T> tClass, int limit, int page);

	PaginationRequest<T> getAsPaginationRequest(Class<T> tClass, int limit);

	PaginationRequest<T> getAsPaginationRequest(Class<T> tClass);

	PaginationRequest<T> getAsPaginationRequest(Class<T> tClass, PaginationRequest<T> paginationRequest);
}