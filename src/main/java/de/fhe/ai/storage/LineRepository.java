package de.fhe.ai.storage;

import de.fhe.ai.RepositoryFactory;
import de.fhe.ai.model.Line;
import de.fhe.ai.model.ModelBase;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class LineRepository implements ILineRepository {

    // Temporary solution => Java2 using database
    private final List<ModelBase> lines = new ArrayList<>();

    public LineRepository(RepositoryFactory repositoryFactory) {
        //TODO Use repositories for database interaction
    }

    @Override
    public void insert(ModelBase entity, Runnable callback) {
        //TODO 2nd java-semester for database

        if(!(entity instanceof Line)) return;

        List<ModelBase> result = lines.stream().filter(modelBase -> modelBase.getId() == entity.getId()).collect(Collectors.toList());
        if(result.isEmpty())
            lines.add(entity);
    }

    @Override
    public void update(ModelBase entity) {
        //TODO 2nd java-semester for database

        if(!(entity instanceof Line)) return;

        List<ModelBase> result = lines.stream().filter(modelBase -> modelBase.getId() == entity.getId()).collect(Collectors.toList());
        if(result.isEmpty())
            lines.add(entity);
        else {
            int index = lines.indexOf(result.get(0));
            lines.set(index, entity);
        }
    }

    @Override
    public void delete(ModelBase entity) {
        //TODO 2nd java-semester for database

        if(!(entity instanceof Line)) return;

        lines.removeIf(modelBase -> modelBase.equals(entity));
    }

    @Override
    public List<ModelBase> getAllEntities() {
        //TODO 2nd java-semester for database

        return lines;
    }

    @Override
    public ModelBase getEntityById(int id) {
        //TODO 2nd java-semester for database

        List<ModelBase> result = lines.stream().filter(modelBase -> modelBase.getId() == id ).collect(Collectors.toList());
        return result.isEmpty() ? null : result.get(0);
    }
}
