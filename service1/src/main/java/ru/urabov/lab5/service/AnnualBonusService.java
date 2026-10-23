package ru.urabov.lab5.service;

import ru.urabov.lab5.model.Positions;

/**
 * Расчёт премий сотрудника.
 */
public interface AnnualBonusService {

    // Годовая премия
    double calculate(Positions positions, double salary, double bonus, int workDays);

    // Квартальная премия, только для управленцев
    double calculateQuarterlyBonus(Positions positions, double salary, double bonus, int workDays);
}
