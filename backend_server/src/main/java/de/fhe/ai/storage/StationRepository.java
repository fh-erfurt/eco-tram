package de.fhe.ai.storage;

import de.fhe.ai.RepositoryFactory;
import de.fhe.ai.model.ModelBase;
import de.fhe.ai.model.Station;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class StationRepository implements IStationRepository {

    // Temporary solution => Java2 using database
    private final List<ModelBase> stations = new ArrayList<>();

    public StationRepository(RepositoryFactory repositoryFactory) {
        //TODO Use repositories for database interaction
    }

    @Override
    public void insert(ModelBase entity, Runnable callback) {
        //TODO 2nd java-semester for database

        if (!(entity instanceof Station)) return;

        List<ModelBase> result = stations.stream().filter(modelBase -> modelBase.getId() == entity.getId()).collect(Collectors.toList());
        if (result.isEmpty())
            stations.add(entity);
    }

    @Override
    public void update(ModelBase entity) {
        //TODO 2nd java-semester for database

        if (!(entity instanceof Station)) return;

        List<ModelBase> result = stations.stream().filter(modelBase -> modelBase.getId() == entity.getId()).collect(Collectors.toList());
        if (result.isEmpty())
            stations.add(entity);
        else {
            int index = stations.indexOf(result.get(0));
            stations.set(index, entity);
        }
    }

    @Override
    public void delete(ModelBase entity) {
        //TODO 2nd java-semester for database

        if (!(entity instanceof Station)) return;

        stations.removeIf(modelBase -> modelBase.equals(entity));
    }

    @Override
    public List<ModelBase> getAllEntities() {
        //TODO 2nd java-semester for database

        return stations;
    }

    @Override
    public ModelBase getEntityById(int id) {
        //TODO 2nd java-semester for database

        List<ModelBase> result = stations.stream().filter(modelBase -> modelBase.getId() == id).collect(Collectors.toList());
        return result.isEmpty() ? null : result.get(0);
    }
}
