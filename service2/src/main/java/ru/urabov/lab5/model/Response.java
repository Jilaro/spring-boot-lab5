package ru.urabov.lab5.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Ответное сообщение (response).
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Response {

    /**
     * Уникальный идентификатор сообщения, копируется из request
     */
    private String uid;

    /**
     * Уникальный идентификатор операции
     */
    private String operationUid;

    /**
     * Время формирования ответа (UTC)
     */
    private String systemTime;

    /**
     * Результат обработки: success или failed
     */
    private Codes code;

    /**
     * Годовая премия сотрудника
     */
    private Double annualBonus;

    /**
     * Квартальная премия, только для управленцев, у остальных null
     */
    private Double quarterlyBonus;

    /**
     * Код ошибки: ValidationException, UnsupportedCodeException, UnknownException или пустая строка
     */
    private ErrorCodes errorCode;

    /**
     * Сообщение об ошибке или пустая строка
     */
    private ErrorMessages errorMessage;
}
