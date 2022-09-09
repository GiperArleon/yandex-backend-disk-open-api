package ru.yandex.backend.files.service;

import ru.yandex.backend.files.model.dto.SystemItemHistoryResponse;
import ru.yandex.backend.files.model.dto.SystemItemImportRequest;
import java.time.ZonedDateTime;

public interface HistoryService {
    void saveFilesHistory(SystemItemImportRequest systemItemImportRequest);
    SystemItemHistoryResponse getUpdatedHistory(String id, ZonedDateTime dateStart, ZonedDateTime dateEnd);
}
