package ubb.tourism.data.validator;

import ubb.tourism.data.exception.ValidationException;

public interface Validator<E> {

    void validate(E entity) throws ValidationException;
}
