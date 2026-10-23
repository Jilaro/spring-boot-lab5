package ru.urabov.lab5.service;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.EnumSource;
import ru.urabov.lab5.model.Positions;

import java.time.Clock;
import java.time.Instant;
import java.time.ZoneOffset;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.assertj.core.api.Assertions.within;

class AnnualBonusServiceImplTest {

    private static final double SALARY = 100000.00;
    private static final double BONUS = 2.0;
    private static final int WORK_DAYS = 243;
    private static final double PRECISION = 1e-6;

    // Сервис с часами, остановленными на заданной дате
    private static AnnualBonusServiceImpl serviceAt(String date) {
        Clock clock = Clock.fixed(Instant.parse(date + "T12:00:00Z"), ZoneOffset.UTC);
        return new AnnualBonusServiceImpl(clock);
    }

    @Test
    @DisplayName("Годовая премия в обычном году (365 дней)")
    void calculate() {
        // given
        Positions position = Positions.HR;

        // when
        double result = serviceAt("2026-10-24").calculate(position, SALARY, BONUS, WORK_DAYS);

        // then: 100000 * 2 * 365 * 1.2 / 243
        double expected = 360493.8271604938;
        assertThat(result).isCloseTo(expected, within(PRECISION));
    }

    @Test
    @DisplayName("Годовая премия в високосном году (366 дней)")
    void calculateInLeapYear() {
        double result = serviceAt("2024-10-24").calculate(Positions.HR, SALARY, BONUS, WORK_DAYS);

        // 100000 * 2 * 366 * 1.2 / 243
        assertThat(result).isCloseTo(361481.48148148146, within(PRECISION));
    }

    @Test
    @DisplayName("Квартальная премия управленца в I квартале обычного года (90 дней)")
    void calculateQuarterlyBonus() {
        // given
        Positions position = Positions.TL;

        // when
        double result = serviceAt("2026-02-15").calculateQuarterlyBonus(position, SALARY, BONUS, WORK_DAYS);

        // then: 100000 * 2 * 90 * 2.6 / 243
        assertThat(result).isCloseTo(192592.59259259258, within(PRECISION));
    }

    @Test
    @DisplayName("Квартальная премия в I квартале високосного года (91 день)")
    void calculateQuarterlyBonusInLeapYear() {
        double result = serviceAt("2024-02-15").calculateQuarterlyBonus(Positions.TL, SALARY, BONUS, WORK_DAYS);

        // 100000 * 2 * 91 * 2.6 / 243
        assertThat(result).isCloseTo(194732.51028806585, within(PRECISION));
    }

    @Test
    @DisplayName("Квартальная премия CTO в III квартале (92 дня)")
    void calculateQuarterlyBonusForCto() {
        double result = serviceAt("2026-08-01").calculateQuarterlyBonus(Positions.CTO, SALARY, BONUS, 247);

        // 100000 * 2 * 92 * 3.6 / 247
        assertThat(result).isCloseTo(268178.1376518219, within(PRECISION));
    }

    @ParameterizedTest(name = "{0} не управленец")
    @EnumSource(value = Positions.class, names = {"DEV", "HR"})
    @DisplayName("Квартальная премия не начисляется не управленцам")
    void calculateQuarterlyBonusForNotManager(Positions position) {
        AnnualBonusServiceImpl service = serviceAt("2026-10-24");

        assertThatThrownBy(() -> service.calculateQuarterlyBonus(position, SALARY, BONUS, WORK_DAYS))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("только управленцам");
    }

    @Test
    @DisplayName("Признак isManager у всех должностей")
    void isManager() {
        assertThat(Positions.DEV.isManager()).isFalse();
        assertThat(Positions.HR.isManager()).isFalse();
        assertThat(Positions.TL.isManager()).isTrue();
        assertThat(Positions.PO.isManager()).isTrue();
        assertThat(Positions.TPM.isManager()).isTrue();
        assertThat(Positions.CTO.isManager()).isTrue();
    }
}
