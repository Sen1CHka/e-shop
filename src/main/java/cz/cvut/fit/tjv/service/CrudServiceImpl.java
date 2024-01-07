package cz.cvut.fit.tjv.service;

import cz.cvut.fit.tjv.domain.EntityWithId;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.CrudRepository;
import java.util.Optional;

public abstract class CrudServiceImpl<T extends EntityWithId<ID>, ID> implements CrudService<T, ID> {
    @Override
    public T create(T e) {
        if(getRepository().findById(e.getId()).isPresent()) throw new RuntimeException("Id already exist");
        return getRepository().save(e);
    }

    @Override
    public Optional<T> readById(ID id) {
        return getRepository().findById(id);
    }

    @Override
    public Iterable<T> readAll() {
        return getRepository().findAll();
    }

    @Override
    public ID deleteById(ID id) {
        if(!getRepository().findById(id).isPresent()) throw new RuntimeException("Id does not exist");
        getRepository().deleteById(id);
        return id;
    }

    protected abstract CrudRepository<T, ID> getRepository();

}
