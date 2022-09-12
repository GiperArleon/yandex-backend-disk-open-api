package ru.yandex.backend.files.validation;

import org.springframework.stereotype.Component;
import ru.yandex.backend.files.exceptions.ValidationException;
import ru.yandex.backend.files.model.SystemItemType;
import ru.yandex.backend.files.model.dto.SystemItemImport;
import ru.yandex.backend.files.model.dto.SystemItemImportRequest;
import java.time.*;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.List;
import java.util.stream.Collectors;

@Component
public class FileValidator {
    private static final int URL_MAX_SIZE = 255;
    private static final int ID_MAX_SIZE = 100;
    private static final long YEARS_SHIFT = 1000;
    private static final long DAYS_SHIFT = 1;
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
        validateUnicNames(systemItemImportRequest.getItems());
    }

    public void validateUnicNames(List<SystemItemImport> testList) {
        List<String> ids = testList.stream()
                .map(SystemItemImport::getId)
                .distinct()
                .collect(Collectors.toList());
        if(ids.size()!=testList.size())
            throw new ValidationException("validation failed, the same ids present");
    }

    public void validateSystemItemImport(SystemItemImport systemItemImport) {
        if(systemItemImport == null
        || systemItemImport.getId() == null
        || systemItemImport.getType() == null) {
            throw new ValidationException("NotNull validation failed");
        }

        validateSystemItemImportIdSize(systemItemImport.getId());
        validateSystemItemType(systemItemImport.getType());

        if(systemItemImport.getType() == SystemItemType.FOLDER) {
            if(systemItemImport.getUrl() != null)
                throw new ValidationException("validation failed, filled url for folder type");
            if(systemItemImport.getSize() != null)
                throw new ValidationException("validation failed, file size != null for folder type");
        }

        if(systemItemImport.getType() == SystemItemType.FILE) {
            if(systemItemImport.getSize() == null)
                throw new ValidationException("validation failed, file size == null for file type");

            if(systemItemImport.getSize() <= 0)
                throw new ValidationException("validation failed, file size <= 0");

            if(systemItemImport.getUrl() == null)
                throw new ValidationException("NotNull validation failed, null url for file type");

            validateSystemItemImportUrlSize(systemItemImport.getUrl());
        }
    }

    public void validateSystemItemType(SystemItemType type) {
        SystemItemType[] aFruits = SystemItemType.values();
        for(SystemItemType aFruit : aFruits)
            if(aFruit.equals(type))
                return;
        throw new ValidationException("Wrong type: " + type.name());
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

    public ZonedDateTime checkToTime(ZonedDateTime toTime) {
        if(toTime == null) {
            return ZonedDateTime.now();
        }
        return toTime;
    }

    public ZonedDateTime makeFromTime(ZonedDateTime toTime) {
        ZonedDateTime to = checkToTime(toTime);
        return to.minusDays(DAYS_SHIFT);
    }

    public ZonedDateTime checkStartTime(ZonedDateTime dateStart) {
        if(dateStart == null) {
            return LocalDateTime.of(LocalDate.EPOCH, LocalTime.MIN).atZone(ZoneId.systemDefault());
        }
        return dateStart;
    }

    public ZonedDateTime checkEndTime(ZonedDateTime dateEnd) {
        if(dateEnd == null) {
            return LocalDateTime.of(LocalDate.now().plusYears(YEARS_SHIFT), LocalTime.MAX).atZone(ZoneId.systemDefault());
        }
        return dateEnd;
    }
}
