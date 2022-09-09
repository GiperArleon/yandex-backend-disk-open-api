package ru.yandex.backend.files.model.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Data;
import lombok.experimental.SuperBuilder;
import org.springframework.http.HttpStatus;

@Data
@SuperBuilder
@JsonInclude(JsonInclude.Include.NON_NULL)
public class GeneralResponse {
    protected int code;
    protected String message;
    protected HttpStatus status;

    public static GeneralResponse getResponse(String message, HttpStatus status) {
        return GeneralResponse.builder()
                .message(message)
                .code(status.value())
                .build();
    }
}
