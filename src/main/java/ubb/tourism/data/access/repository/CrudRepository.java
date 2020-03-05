package ubb.tourism.data.access.repository;

public interface CrudRepository<ID, T> {

    int size();

    void save(T entity);

    void update(ID id, T entity);

    void delete(ID id);

    T findOne(ID id);

    Iterable<T> findAll();
}
