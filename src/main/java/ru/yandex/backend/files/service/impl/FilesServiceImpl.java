package ru.yandex.backend.files.service.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.yandex.backend.files.exceptions.ObjectNotFoundException;
import ru.yandex.backend.files.mapper.FilesMapper;
import ru.yandex.backend.files.model.dto.SystemItem;
import ru.yandex.backend.files.model.dto.SystemItemHistoryResponse;
import ru.yandex.backend.files.model.dto.SystemItemImportRequest;
import ru.yandex.backend.files.model.entity.Item;
import ru.yandex.backend.files.repository.FilesRepository;
import ru.yandex.backend.files.service.FilesService;
import ru.yandex.backend.files.validation.FileValidator;
import java.time.ZonedDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional
public class FilesServiceImpl implements FilesService {
    private final FilesRepository filesRepository;
    private final FilesMapper filesMapper;
    private final FileValidator fileValidator;

    @Override
    public void saveFiles(SystemItemImportRequest systemItemImportRequest) {
        fileValidator.validateSystemItemImportRequest(systemItemImportRequest);
        filesMapper.itemsFromSystemItemImportRequest(systemItemImportRequest)
                .forEach(filesRepository::save);
    }

    @Override
    public SystemItem getFileById(String id) {
        Item item = filesRepository.findById(id)
                .orElseThrow(() -> new ObjectNotFoundException("Item " + id + " not found"));
        return filesMapper.systemItemFromItem(item);
    }

    @Override
    public void deleteFileById(String id) {
        fileValidator.validateNotNull(id);
        getFileById(id);
        filesRepository.deleteById(id);
    }

    @Override
    public SystemItemHistoryResponse findUpdatesByDate(ZonedDateTime to) {
        List<Item> items = filesRepository.findFilesByUpdateTime(fileValidator.makeFromTime(to), fileValidator.checkToTime(to));
        return filesMapper.systemItemHistoryResponseFromItems(items);
    }
}
