# spring-boot-lab5

Лабораторная работа №5 по дисциплине «Технологии программирования».
Выполнил: Урабов Николай Михайлович, группа РИЗ-250213у.

Доработка Сервиса 1 из ЛР 4: расчёт годовой и квартальной премии и unit-тесты на JUnit 5.
Spring Boot 4.1.2, Java 25, Maven, Lombok, Validation.

| Модуль | Порт | Назначение |
|--------|------|------------|
| `service1` | 8082 | Принимает request, считает премии, пересылает request в Сервис 2 |
| `service2` | 8084 | Принимает request от Сервиса 1 и пишет в лог время передачи |

## Расчёт премий

```
годовая    = salary * bonus * (дней в году) * positionCoefficient / workDays
квартальная = salary * bonus * (дней в текущем квартале) * positionCoefficient / workDays
```

Дней в году – 365 или 366 (високосный). Квартальная премия начисляется только
должностям с `isManager = true`: TL, PO, TPM, CTO.

## Запуск

1. Запустить `Service2Application`, затем `Service1Application`.
2. Импортировать в Postman `postman/lab5.postman_collection.json`.

## Тесты

`service1/src/test/java/ru/urabov/lab5/service/AnnualBonusServiceImplTest.java` –
правой кнопкой по классу → Run.
