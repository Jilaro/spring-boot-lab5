package ru.urabov.lab5.service;

import ru.urabov.lab5.model.Request;

/**
 * Изменение request перед пересылкой в Сервис 2.
 */
public interface ModifyRequestService {

    void modify(Request request);
}
