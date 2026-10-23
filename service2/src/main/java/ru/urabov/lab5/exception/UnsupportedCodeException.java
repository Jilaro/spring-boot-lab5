package ru.urabov.lab5.exception;

/**
 * Собственное исключение: выбрасывается, если uid равен 123.
 */
public class UnsupportedCodeException extends Exception {

    public UnsupportedCodeException(String message) {
        super(message);
    }
}
