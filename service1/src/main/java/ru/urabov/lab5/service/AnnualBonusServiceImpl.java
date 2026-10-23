package ru.urabov.lab5.service;

import org.springframework.stereotype.Service;
import ru.urabov.lab5.model.Positions;

import java.time.Clock;
import java.time.LocalDate;
import java.time.Year;
import java.time.temporal.IsoFields;

@Service
public class AnnualBonusServiceImpl implements AnnualBonusService {

    // Часы, по которым определяется текущий год и квартал
    private final Clock clock;

    // Конструктор для Spring: текущее время системы
    public AnnualBonusServiceImpl() {
        this(Clock.systemDefaultZone());
    }

    // Конструктор для тестов: можно задать любую дату
    public AnnualBonusServiceImpl(Clock clock) {
        this.clock = clock;
    }

    @Override
    public double calculate(Positions positions, double salary, double bonus, int workDays) {
        // 366 дней в високосном году, 365 – в обычном
        int daysInYear = Year.now(clock).length();
        return salary * bonus * daysInYear * positions.getPositionCoefficient() / workDays;
    }

    @Override
    public double calculateQuarterlyBonus(Positions positions, double salary, double bonus, int workDays) {
        if (!positions.isManager()) {
            throw new IllegalArgumentException(
                    "Квартальная премия начисляется только управленцам, " + positions + " не управленец");
        }
        // Та же формула, что для годовой премии, но вместо дней в году – дни текущего квартала
        int daysInQuarter = daysInCurrentQuarter();
        return salary * bonus * daysInQuarter * positions.getPositionCoefficient() / workDays;
    }

    // Количество дней в текущем квартале: 90, 91 или 92 (в I квартале високосного года – 91)
    private int daysInCurrentQuarter() {
        LocalDate today = LocalDate.now(clock);
        LocalDate quarterStart = today.with(IsoFields.DAY_OF_QUARTER, 1);
        return (int) (quarterStart.plusMonths(3).toEpochDay() - quarterStart.toEpochDay());
    }
}
