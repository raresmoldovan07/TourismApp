package tourism.app.persistence.data.access.repository;

import tourism.app.model.entity.User;

public interface UserRepository extends CrudRepository<Integer, User> {

    User getUserByUsernameAndPassword(String username, String password);
}
