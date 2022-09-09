package ru.yandex.backend.files.service;

import ru.yandex.backend.files.model.dto.SystemItem;
import ru.yandex.backend.files.model.dto.SystemItemHistoryResponse;
import ru.yandex.backend.files.model.dto.SystemItemImportRequest;
import java.time.ZonedDateTime;

public interface FilesService {
    void saveFiles(SystemItemImportRequest systemItemImportRequest);

    SystemItem getFileById(String id);

    void deleteFileById(String id);

    SystemItemHistoryResponse findUpdatesByDate(ZonedDateTime updateTime);
}
