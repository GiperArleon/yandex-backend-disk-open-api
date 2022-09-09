package ru.yandex.backend.files.exceptions;

import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;
import ru.yandex.backend.files.model.dto.GeneralResponse;
import java.time.format.DateTimeParseException;

public class ExceptionHandler  extends ResponseEntityExceptionHandler {

    @org.springframework.web.bind.annotation.ExceptionHandler(value = {
            MappingException.class,
            ValidationException.class,
            DateTimeParseException.class,
            IllegalArgumentException.class})
    protected ResponseEntity<Object> badRequest(RuntimeException ex, WebRequest request) {
        GeneralResponse rsp = GeneralResponse.getResponse(
                ex.getMessage(),
                HttpStatus.BAD_REQUEST);
        return handleExceptionInternal(ex, rsp,
                new HttpHeaders(), HttpStatus.BAD_REQUEST, request);
    }

    @org.springframework.web.bind.annotation.ExceptionHandler(value = {
            ObjectNotFoundException.class})
    protected ResponseEntity<Object> notFound(RuntimeException ex, WebRequest request) {
        GeneralResponse rsp = GeneralResponse.getResponse(
                ex.getMessage(),
                HttpStatus.NOT_FOUND);
        return handleExceptionInternal(ex, rsp,
                new HttpHeaders(), HttpStatus.NOT_FOUND, request);
    }
}
