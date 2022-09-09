package ru.yandex.backend.files.validation;

import org.springframework.stereotype.Component;
import ru.yandex.backend.files.exceptions.ValidationException;
import ru.yandex.backend.files.model.SystemItemType;
import ru.yandex.backend.files.model.dto.SystemItemImport;
import ru.yandex.backend.files.model.dto.SystemItemImportRequest;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;

@Component
public class FileValidator {
    private static final int URL_MAX_SIZE = 255;
    private static final int ID_MAX_SIZE = 100;
    private final DateTimeFormatter dateFormatter = DateTimeFormatter.ISO_ZONED_DATE_TIME;

    public void validateNotNull(Object object) {
        if(object == null) {
            throw new ValidationException("NotNull validation failed");
        }
    }

    public void validateSystemItemImportRequest(SystemItemImportRequest systemItemImportRequest) {
        if(systemItemImportRequest == null
                || systemItemImportRequest.getItems().isEmpty()
                || systemItemImportRequest.getUpdateDate() == null) {
            throw new ValidationException("NotNull validation failed");
        }
        validDateTimeNew(systemItemImportRequest.getUpdateDate().toString());
    }

    public void validateSystemItemImport(SystemItemImport systemItemImport) {
        if(systemItemImport == null
        || systemItemImport.getId() == null
        || systemItemImport.getType() == null) {
            throw new ValidationException("NotNull validation failed");
        }

        validateSystemItemImportIdSize(systemItemImport.getId());

        if(systemItemImport.getType() == SystemItemType.FILE) {
            if(systemItemImport.getUrl() == null)
                throw new ValidationException("NotNull validation failed, null url for file type");
            validateSystemItemImportUrlSize(systemItemImport.getUrl());
        }
    }

    public void validateSystemItemImportIdSize(String id) {
        if(id.isEmpty()
        || id.length() > ID_MAX_SIZE) {
            throw new ValidationException("Id should be from 1 to 100 characters long");
        }
    }

    public void validateSystemItemImportUrlSize(String url) {
        if(url.isEmpty()
        || url.length() > URL_MAX_SIZE) {
            throw new ValidationException("Url should be from 1 to 255 characters long");
        }
    }

    public void validDateTimeNew(String dateStr) {
        try {
            dateFormatter.parse(dateStr);
        } catch(DateTimeParseException | IllegalArgumentException ex) {
            throw new ValidationException("Date validation failed", ex.getMessage());
        }
    }
}
