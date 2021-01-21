package de.fhe.ai.storage;

import java.util.List;

/**
 * A base repository interface providing some standard method for storage handling.
 *
 * @param <T> This needs to be a Model Entity
 */
public interface IBaseRepository<T> {

    /**
     * Saves an entity to the storage.
     * @param entity The entity that will be inserted.
     */
    void create(T entity, Runnable callback);

    /**
     * Updates an existing entity inside the storage.
     * @param entity The entity that will be updated.
     */
    void update(T entity);

    /**
     * Deletes a given entity from the database.
     * @param entity The entity that will be deleted.
     */
    void delete(T entity);

    /**
     * Gets a list of all entities of this table from the storage.
     * @return A list of all entities.
     */
    List<T> getAllEntities();

    /**
     * Gets an entity from the storage by there ID.
     * @return An entity with the given id.
     */
    T getEntityById(int id);
}
