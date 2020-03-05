package ubb.tourism.data.access.repository.impl;

import ubb.tourism.data.access.entity.Entity;
import ubb.tourism.data.access.repository.CrudRepository;
import ubb.tourism.data.validator.Validator;

import java.util.HashMap;
import java.util.Map;

public class AbstractRepository<ID, T extends Entity<ID>> implements CrudRepository<ID, T> {

    private Map<ID, T> entities;
    private Validator<T> validator;

    public AbstractRepository(Validator<T> validator) {
        this.validator = validator;
        entities = new HashMap<>();
    }

    @Override
    public int size() {
        return 0;
    }

    @Override
    public void save(T entity) {

    }

    @Override
    public void update(ID id, T entity) {

    }

    @Override
    public void delete(ID id) {

    }

    @Override
    public T findOne(ID id) {
        return null;
    }

    @Override
    public Iterable<T> findAll() {
        return null;
    }
}
