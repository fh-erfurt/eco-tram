package de.fhe.ai.storage;

import de.fhe.ai.model.ModelBase;

import java.util.List;

/**
 * A base repository interface providing some standard method for storage handling.
 */
public interface IBaseRepository {

    /**
     * Saves an entity to the storage.
     *
     * @param entity The entity that will be inserted.
     */
    void insert(ModelBase entity, Runnable callback);

    /**
     * Updates an existing entity inside the storage.
     *
     * @param entity The entity that will be updated.
     */
    void update(ModelBase entity);

    /**
     * Deletes a given entity from the database.
     *
     * @param entity The entity that will be deleted.
     */
    void delete(ModelBase entity);

    /**
     * Gets a list of all entities of this table from the storage.
     *
     * @return A list of all entities.
     */
    List<ModelBase> getAllEntities();

    /**
     * Gets an entity from the storage by there ID.
     *
     * @return An entity with the given id.
     */
    ModelBase getEntityById(int id);
}
