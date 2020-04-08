package tourism.app.persistence.data.access.repository;

import tourism.app.persistence.data.access.entity.User;

public interface UserRepository extends CrudRepository<Integer, User> {

    User getUserByUsernameAndPassword(String username, String password);
}
