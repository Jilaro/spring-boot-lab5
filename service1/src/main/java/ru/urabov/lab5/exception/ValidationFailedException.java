package ru.urabov.lab5.exception;

/**
 * Выбрасывается, если входящее сообщение не прошло валидацию.
 */
public class ValidationFailedException extends Exception {

    public ValidationFailedException(String message) {
        super(message);
    }
}
