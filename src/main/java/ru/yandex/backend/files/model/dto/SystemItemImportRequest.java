package ru.yandex.backend.files.model.dto;

import lombok.Data;
import java.time.ZonedDateTime;
import java.util.List;

@Data
public class SystemItemImportRequest {
    private List<SystemItemImport> items;
    private ZonedDateTime updateDate;
}
