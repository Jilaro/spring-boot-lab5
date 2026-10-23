package ru.urabov.lab5.service;

import ru.urabov.lab5.model.Response;

/**
 * Изменение ответа перед отправкой клиенту.
 */
public interface ModifyResponseService {

    Response modify(Response response);
}
