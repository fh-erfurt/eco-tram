package de.fhe.ai.storage;

import de.fhe.ai.RepositoryFactory;
import de.fhe.ai.model.Connection;
import de.fhe.ai.model.ModelBase;

import java.util.ArrayList;
import java.util.List;

public class ConnectionRepository implements IConnectionRepository {

    // Temporary solution => Java2 using database
    private List<ModelBase> connections = new ArrayList<>();


    public ConnectionRepository(RepositoryFactory repositoryFactory) {
        //TODO Use repositories for database interaction
    }

    @Override
    public void insert(ModelBase entity, Runnable callback) {
        //TODO 2nd java-semester for database
    }

    @Override
    public void update(ModelBase entity) {
        //TODO 2nd java-semester for database
    }

    @Override
    public void delete(ModelBase entity) {
        //TODO 2nd java-semester for database
    }

    @Override
    public List<ModelBase> getAllEntities() {
        return connections;
    }

    @Override
    public ModelBase getEntityById(int id) {
        return null;
    }



}
