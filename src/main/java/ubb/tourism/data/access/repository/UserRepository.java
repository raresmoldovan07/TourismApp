package ubb.tourism.data.access.repository;

import ubb.tourism.data.access.entity.User;

public interface UserRepository extends CrudRepository<Integer, User> {

    User getUserByUsernameAndPassword(String username, String password);
}
