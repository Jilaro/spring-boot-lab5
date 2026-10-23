package ru.urabov.lab5.service;

import org.springframework.validation.BindingResult;
import ru.urabov.lab5.exception.UnsupportedCodeException;
import ru.urabov.lab5.exception.ValidationFailedException;
import ru.urabov.lab5.model.Request;

public interface ValidationService {

    // Проверяет результат валидации полей запроса
    void isValid(BindingResult bindingResult) throws ValidationFailedException;

    // Проверяет, что uid поддерживается сервисом
    void checkUid(Request request) throws UnsupportedCodeException;
}
