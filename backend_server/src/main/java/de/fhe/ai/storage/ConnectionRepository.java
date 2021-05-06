package de.fhe.ai.storage;

import de.fhe.ai.RepositoryFactory;
import de.fhe.ai.model.Connection;
import de.fhe.ai.model.ModelBase;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class ConnectionRepository implements IConnectionRepository {

    // Temporary solution => Java2 using database
    private final List<ModelBase> connections = new ArrayList<>();

    public ConnectionRepository(RepositoryFactory repositoryFactory) {
        //TODO Use repositories for database interaction
    }

    @Override
    public void insert(ModelBase entity, Runnable callback) {
        //TODO 2nd java-semester for database

        if (!(entity instanceof Connection)) return;

        List<ModelBase> result = connections.stream().filter(modelBase -> modelBase.getId() == entity.getId()).collect(Collectors.toList());
        if (result.isEmpty())
            connections.add(entity);
    }

    @Override
    public void update(ModelBase entity) {
        //TODO 2nd java-semester for database

        if (!(entity instanceof Connection)) return;

        List<ModelBase> result = connections.stream().filter(modelBase -> modelBase.getId() == entity.getId()).collect(Collectors.toList());
        if (result.isEmpty())
            connections.add(entity);
        else {
            int index = connections.indexOf(result.get(0));
            connections.set(index, entity);
        }
    }

    @Override
    public void delete(ModelBase entity) {
        //TODO 2nd java-semester for database

        if (!(entity instanceof Connection)) return;

        connections.removeIf(modelBase -> modelBase.equals(entity));
    }

    @Override
    public List<ModelBase> getAllEntities() {
        //TODO 2nd java-semester for database

        return connections;
    }

    @Override
    public ModelBase getEntityById(int id) {
        //TODO 2nd java-semester for database

        List<ModelBase> result = connections.stream().filter(modelBase -> modelBase.getId() == id).collect(Collectors.toList());
        return result.isEmpty() ? null : result.get(0);
    }
}
