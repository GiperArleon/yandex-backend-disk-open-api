package ru.yandex.backend.files.exceptions;

import org.springframework.beans.ConversionNotSupportedException;
import org.springframework.beans.TypeMismatchException;
import org.springframework.http.*;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.MissingPathVariableException;
import org.springframework.web.bind.MissingServletRequestParameterException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.multipart.support.MissingServletRequestPartException;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;
import ru.yandex.backend.files.model.dto.GeneralResponse;
import java.time.format.DateTimeParseException;

@ControllerAdvice
public class ExceptionHandler extends ResponseEntityExceptionHandler {

    @org.springframework.web.bind.annotation.ExceptionHandler(value = {
            MappingException.class,
            ValidationException.class,
            DateTimeParseException.class,
            IllegalArgumentException.class})
    protected ResponseEntity<Object> badRequest(RuntimeException ex, WebRequest request) {
        GeneralResponse rsp = GeneralResponse.getResponse(
                "Validation Failed",
                HttpStatus.BAD_REQUEST);
        return handleExceptionInternal(ex, rsp,
                new HttpHeaders(), HttpStatus.BAD_REQUEST, request);
    }

    @org.springframework.web.bind.annotation.ExceptionHandler(value = {
            ObjectNotFoundException.class})
    protected ResponseEntity<Object> notFound(RuntimeException ex, WebRequest request) {
        GeneralResponse rsp = GeneralResponse.getResponse(
                "Item not found",
                HttpStatus.NOT_FOUND);
        return handleExceptionInternal(ex, rsp,
                new HttpHeaders(), HttpStatus.NOT_FOUND, request);
    }

    @Override
    protected ResponseEntity<Object> handleHttpMessageNotReadable(HttpMessageNotReadableException ex,
                                                                  HttpHeaders headers,
                                                                  HttpStatus status,
                                                                  WebRequest request) {
        GeneralResponse rsp = GeneralResponse.getResponse(
                "Validation Failed",
                HttpStatus.BAD_REQUEST);
        return new ResponseEntity<>(rsp, HttpStatus.BAD_REQUEST);
    }

    @Override
    protected ResponseEntity<Object> handleMissingPathVariable(MissingPathVariableException ex, HttpHeaders headers, HttpStatus status, WebRequest request) {
        GeneralResponse rsp = GeneralResponse.getResponse(
                "Validation Failed",
                HttpStatus.BAD_REQUEST);
        return new ResponseEntity<>(rsp, HttpStatus.BAD_REQUEST);
    }

    @Override
    protected ResponseEntity<Object> handleMissingServletRequestParameter(MissingServletRequestParameterException ex, HttpHeaders headers, HttpStatus status, WebRequest request) {
        GeneralResponse rsp = GeneralResponse.getResponse(
                "Validation Failed",
                HttpStatus.BAD_REQUEST);
        return new ResponseEntity<>(rsp, HttpStatus.BAD_REQUEST);
    }

    @Override
    protected ResponseEntity<Object> handleConversionNotSupported(ConversionNotSupportedException ex, HttpHeaders headers, HttpStatus status, WebRequest request) {
        GeneralResponse rsp = GeneralResponse.getResponse(
                "Validation Failed",
                HttpStatus.BAD_REQUEST);
        return new ResponseEntity<>(rsp, HttpStatus.BAD_REQUEST);
    }

    @Override
    protected ResponseEntity<Object> handleTypeMismatch(TypeMismatchException ex, HttpHeaders headers, HttpStatus status, WebRequest request) {
        GeneralResponse rsp = GeneralResponse.getResponse(
                "Validation Failed",
                HttpStatus.BAD_REQUEST);
        return new ResponseEntity<>(rsp, HttpStatus.BAD_REQUEST);
    }

    @Override
    protected ResponseEntity<Object> handleMethodArgumentNotValid(MethodArgumentNotValidException ex, HttpHeaders headers, HttpStatus status, WebRequest request) {
        GeneralResponse rsp = GeneralResponse.getResponse(
                "Validation Failed",
                HttpStatus.BAD_REQUEST);
        return new ResponseEntity<>(rsp, HttpStatus.BAD_REQUEST);
    }

    @Override
    protected ResponseEntity<Object> handleMissingServletRequestPart(MissingServletRequestPartException ex, HttpHeaders headers, HttpStatus status, WebRequest request) {
        GeneralResponse rsp = GeneralResponse.getResponse(
                "Validation Failed",
                HttpStatus.BAD_REQUEST);
        return new ResponseEntity<>(rsp, HttpStatus.BAD_REQUEST);
    }
}
