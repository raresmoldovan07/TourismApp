package tourism.app.persistence.data.validator;

import tourism.app.persistence.data.exception.ValidationException;

public interface Validator<E> {

    void validate(E entity) throws ValidationException;
}
