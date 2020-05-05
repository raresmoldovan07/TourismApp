package tourism.app.persistence.data.validator.impl;

import tourism.app.model.entity.User;
import tourism.app.persistence.data.exception.ValidationException;
import tourism.app.persistence.data.validator.Validator;

public class UserValidator implements Validator<User> {

    @Override
    public void validate(User entity) throws ValidationException {

    }
}
