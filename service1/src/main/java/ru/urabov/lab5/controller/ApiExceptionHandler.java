package ru.urabov.lab5.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import ru.urabov.lab5.model.ErrorCodes;
import ru.urabov.lab5.model.ErrorMessages;
import ru.urabov.lab5.model.Response;
import ru.urabov.lab5.service.FeedbackService;

/**
 * Ошибки, которые возникают до вызова контроллера.
 */
@Slf4j
@RestControllerAdvice
public class ApiExceptionHandler {

    // Тело запроса не удалось прочитать: некорректный JSON или неизвестное значение перечисления
    @ExceptionHandler(HttpMessageNotReadableException.class)
    public ResponseEntity<Response> handleNotReadable(HttpMessageNotReadableException e) {
        log.error("Не удалось прочитать request: {}", e.getMostSpecificCause().getMessage());
        return FeedbackService.failed(FeedbackService.createResponse(null),
                ErrorCodes.VALIDATION_EXCEPTION, ErrorMessages.VALIDATION, HttpStatus.BAD_REQUEST);
    }
}
