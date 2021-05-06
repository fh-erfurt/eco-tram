package de.fhe.ai.storage;

import de.fhe.ai.RepositoryFactory;
import de.fhe.ai.model.ModelBase;
import de.fhe.ai.model.Tram;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class TramRepository implements ITramRepository {

    // Temporary solution => Java2 using database
    private final List<ModelBase> trams = new ArrayList<>();

    public TramRepository(RepositoryFactory repositoryFactory) {
        //TODO Use repositories for database interaction
    }

    @Override
    public void insert(ModelBase entity, Runnable callback) {
        //TODO 2nd java-semester for database

        if (!(entity instanceof Tram)) return;

        List<ModelBase> result = trams.stream().filter(modelBase -> modelBase.getId() == entity.getId()).collect(Collectors.toList());
        if (result.isEmpty())
            trams.add(entity);
    }

    @Override
    public void update(ModelBase entity) {
        //TODO 2nd java-semester for database

        if (!(entity instanceof Tram)) return;

        List<ModelBase> result = trams.stream().filter(modelBase -> modelBase.getId() == entity.getId()).collect(Collectors.toList());
        if (result.isEmpty())
            trams.add(entity);
        else {
            int index = trams.indexOf(result.get(0));
            trams.set(index, entity);
        }
    }

    @Override
    public void delete(ModelBase entity) {
        //TODO 2nd java-semester for database

        if (!(entity instanceof Tram)) return;

        trams.removeIf(modelBase -> modelBase.equals(entity));
    }

    @Override
    public List<ModelBase> getAllEntities() {
        //TODO 2nd java-semester for database

        return trams;
    }

    @Override
    public ModelBase getEntityById(int id) {
        //TODO 2nd java-semester for database

        List<ModelBase> result = trams.stream().filter(modelBase -> modelBase.getId() == id).collect(Collectors.toList());
        return result.isEmpty() ? null : result.get(0);
    }
}
