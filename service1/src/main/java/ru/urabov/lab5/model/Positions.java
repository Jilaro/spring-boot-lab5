package ru.urabov.lab5.model;

/**
 * Должности сотрудников.
 * positionCoefficient – коэффициент должности для расчёта премии,
 * isManager – признак управленца, только им начисляется квартальная премия.
 */
public enum Positions {

    DEV("Developer", 2.2, false),
    HR("HR-specialist", 1.2, false),
    TL("Team Lead", 2.6, true),
    PO("Product Owner", 2.8, true),
    TPM("Technical Product Manager", 3.0, true),
    CTO("Chief Technology Officer", 3.6, true);

    private final String description;
    private final double positionCoefficient;
    private final boolean isManager;

    Positions(String description, double positionCoefficient, boolean isManager) {
        this.description = description;
        this.positionCoefficient = positionCoefficient;
        this.isManager = isManager;
    }

    public String getDescription() {
        return description;
    }

    public double getPositionCoefficient() {
        return positionCoefficient;
    }

    public boolean isManager() {
        return isManager;
    }
}
