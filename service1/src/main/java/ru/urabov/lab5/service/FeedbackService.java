package ru.urabov.lab5.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.validation.BindingResult;
import ru.urabov.lab5.exception.UnsupportedCodeException;
import ru.urabov.lab5.exception.ValidationFailedException;
import ru.urabov.lab5.model.Codes;
import ru.urabov.lab5.model.ErrorCodes;
import ru.urabov.lab5.model.ErrorMessages;
import ru.urabov.lab5.model.Request;
import ru.urabov.lab5.model.Response;
import ru.urabov.lab5.util.DateTimeUtil;

import java.time.Instant;
import java.util.List;

/**
 * Вся обработка request, которая раньше была в MyController.
 */
@Slf4j
@Service
public class FeedbackService {

    private final ValidationService validationService;
    private final ModifyResponseService modifyResponseService;
    private final List<ModifyRequestService> modifyRequestServices;
    private final Service2Client service2Client;
    private final AnnualBonusService annualBonusService;

    public FeedbackService(ValidationService validationService,
                           @Qualifier("ModifyOperationUidResponseService")
                           ModifyResponseService modifyResponseService,
                           List<ModifyRequestService> modifyRequestServices,
                           Service2Client service2Client,
                           AnnualBonusService annualBonusService) {
        this.validationService = validationService;
        this.modifyResponseService = modifyResponseService;
        this.modifyRequestServices = modifyRequestServices;
        this.service2Client = service2Client;
        this.annualBonusService = annualBonusService;
    }

    public ResponseEntity<Response> process(Request request, BindingResult bindingResult, Instant receivedAt) {
        Response response = createResponse(request);
        log.info("Создан response: {}", response);

        try {
            validationService.isValid(bindingResult);
            validationService.checkUid(request);
            calculateBonuses(request, response);
            modifyRequestServices.forEach(service -> service.modify(request));
            log.info("Модифицированный request: {}", request);
            service2Client.send(request, receivedAt);
        } catch (ValidationFailedException e) {
            log.error("Запрос {} не прошёл валидацию: {}", request.getUid(), e.getMessage());
            return failed(response, ErrorCodes.VALIDATION_EXCEPTION, ErrorMessages.VALIDATION, HttpStatus.BAD_REQUEST);
        } catch (UnsupportedCodeException e) {
            log.error("Запрос {} не поддерживается: {}", request.getUid(), e.getMessage());
            return failed(response, ErrorCodes.UNSUPPORTED_EXCEPTION, ErrorMessages.UNSUPPORTED, HttpStatus.BAD_REQUEST);
        } catch (Exception e) {
            log.error("Непредвиденная ошибка при обработке запроса {}: {}", request.getUid(), e.getMessage());
            return failed(response, ErrorCodes.UNKNOWN_EXCEPTION, ErrorMessages.UNKNOWN, HttpStatus.INTERNAL_SERVER_ERROR);
        }

        modifyResponseService.modify(response);
        log.info("Отправлен response: {}", response);
        return ResponseEntity.ok(response);
    }

    // Годовая премия – всем, квартальная – только управленцам
    private void calculateBonuses(Request request, Response response) {
        double annualBonus = annualBonusService.calculate(
                request.getPosition(), request.getSalary(), request.getBonus(), request.getWorkDays());
        response.setAnnualBonus(annualBonus);
        log.info("Годовая премия для {}: {}", request.getPosition(), annualBonus);

        if (request.getPosition().isManager()) {
            double quarterlyBonus = annualBonusService.calculateQuarterlyBonus(
                    request.getPosition(), request.getSalary(), request.getBonus(), request.getWorkDays());
            response.setQuarterlyBonus(quarterlyBonus);
            log.info("Квартальная премия для {}: {}", request.getPosition(), quarterlyBonus);
        } else {
            log.info("{} не управленец, квартальная премия не начисляется", request.getPosition());
        }
    }

    // Ответ с идентификаторами из request и code = success
    public static Response createResponse(Request request) {
        return Response.builder()
                .uid(request == null ? "" : request.getUid())
                .operationUid(request == null ? "" : request.getOperationUid())
                .systemTime(DateTimeUtil.getCustomFormat().format(Instant.now()))
                .code(Codes.SUCCESS)
                .errorCode(ErrorCodes.EMPTY)
                .errorMessage(ErrorMessages.EMPTY)
                .build();
    }

    // Заполняет ответ данными об ошибке
    public static ResponseEntity<Response> failed(Response response, ErrorCodes errorCode,
                                                  ErrorMessages errorMessage, HttpStatus status) {
        response.setCode(Codes.FAILED);
        response.setErrorCode(errorCode);
        response.setErrorMessage(errorMessage);
        log.info("Отправлен response с ошибкой: {}", response);
        return new ResponseEntity<>(response, status);
    }
}
