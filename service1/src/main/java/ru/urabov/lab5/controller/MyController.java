package ru.urabov.lab5.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import ru.urabov.lab5.model.Request;
import ru.urabov.lab5.model.Response;
import ru.urabov.lab5.service.FeedbackService;

import java.time.Instant;

@Slf4j
@RestController
@RequiredArgsConstructor
public class MyController {

    private final FeedbackService feedbackService;

    @PostMapping("/feedback")
    public ResponseEntity<Response> feedback(@Valid @RequestBody Request request,
                                             BindingResult bindingResult) {
        Instant receivedAt = Instant.now();
        log.info("Получен request в {}: {}", receivedAt, request);
        return feedbackService.process(request, bindingResult, receivedAt);
    }
}
