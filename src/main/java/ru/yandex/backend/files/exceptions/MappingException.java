package ru.yandex.backend.files.exceptions;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@Slf4j
@ResponseStatus(code = HttpStatus.BAD_REQUEST, reason = "Wrong mapping")
public class MappingException extends RuntimeException {
    public MappingException(String message) {
        super(message);
        log.error("Ex: {} ", message);
    }
}
