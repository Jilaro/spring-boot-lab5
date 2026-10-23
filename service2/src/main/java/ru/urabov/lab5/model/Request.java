package ru.urabov.lab5.model;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Входящее сообщение (request).
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Request {

    /**
     * Уникальный идентификатор сообщения, обязательный, до 32 символов
     */
    @NotBlank
    @Size(max = 32)
    private String uid;

    /**
     * Уникальный идентификатор операции, обязательный, до 32 символов
     */
    @NotBlank
    @Size(max = 32)
    private String operationUid;

    /**
     * Имя системы отправителя: ERP, CRM, WMS или SERVICE1
     */
    private Systems systemName;

    /**
     * Время создания сообщения, обязательное
     */
    @NotBlank
    private String systemTime;

    /**
     * Наименование ресурса
     */
    private String source;

    /**
     * Должность сотрудника, обязательная
     */
    @NotNull
    private Positions position;

    /**
     * Оклад сотрудника, больше нуля
     */
    @NotNull
    @Positive
    private Double salary;

    /**
     * Коэффициент премии, больше нуля
     */
    @NotNull
    @Positive
    private Double bonus;

    /**
     * Количество рабочих дней в году, от 1 до 366
     */
    @NotNull
    @Min(1)
    @Max(366)
    private Integer workDays;

    /**
     * Уникальный идентификатор коммуникации, от 1 до 100000
     */
    @Min(1)
    @Max(100000)
    private int communicationId;

    /**
     * Уникальный идентификатор шаблона
     */
    private int templateId;

    /**
     * Код продукта
     */
    private int productCode;

    /**
     * Смс код
     */
    private int smsCode;

    // Собственный toString() вместо сгенерированного Lombok: одна строка для логов
    @Override
    public String toString() {
        return "Request{" +
                "uid='" + uid + '\'' +
                ", operationUid='" + operationUid + '\'' +
                ", systemName=" + systemName +
                ", systemTime='" + systemTime + '\'' +
                ", source='" + source + '\'' +
                ", position=" + position +
                ", salary=" + salary +
                ", bonus=" + bonus +
                ", workDays=" + workDays +
                ", communicationId=" + communicationId +
                ", templateId=" + templateId +
                ", productCode=" + productCode +
                ", smsCode=" + smsCode +
                '}';
    }
}
