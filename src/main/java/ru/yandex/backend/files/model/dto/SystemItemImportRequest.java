package ru.yandex.backend.files.model.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import java.time.ZonedDateTime;
import java.util.List;

@Data
@AllArgsConstructor
public class SystemItemImportRequest {
    private List<SystemItemImport> items;
    private ZonedDateTime updateDate;
}
