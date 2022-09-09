package ru.yandex.backend.files.service.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.yandex.backend.files.mapper.FilesMapper;
import ru.yandex.backend.files.mapper.HistoryMapper;
import ru.yandex.backend.files.model.dto.SystemItemHistoryResponse;
import ru.yandex.backend.files.model.dto.SystemItemImportRequest;
import ru.yandex.backend.files.model.entity.History;
import ru.yandex.backend.files.model.entity.Item;
import ru.yandex.backend.files.repository.FilesRepository;
import ru.yandex.backend.files.repository.HistoryRepository;
import ru.yandex.backend.files.service.HistoryService;
import ru.yandex.backend.files.validation.FileValidator;
import java.time.ZonedDateTime;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
@Transactional
public class HistoryServiceImpl implements HistoryService {
    private final FilesRepository productsRepository;
    private final HistoryRepository historyRepository;
    private final HistoryMapper historyMapper;
    private final FilesMapper filesMapper;
    private final FileValidator fileValidator;

    @Override
    public void saveFilesHistory(SystemItemImportRequest systemItemImportRequest) {
        fileValidator.validateSystemItemImportRequest(systemItemImportRequest);
        systemItemImportRequest.getItems().forEach(v->{
            Optional<Item> item = productsRepository.findById(v.getId());
            item.ifPresent(c-> {
                if(c.getItemSize()==null) {
                    c.setItemSize(calcFolderSize(c));
                }
                historyRepository.save(historyMapper.historyFromItem(c));
            });
        });
    }

    @Override
    public SystemItemHistoryResponse getUpdatedHistory(String id, ZonedDateTime dateStart, ZonedDateTime dateEnd) {
        List<History> history;
        if(dateStart == null || dateEnd == null) {
            history = historyRepository.findFullHistory(id);
        } else {
            history = historyRepository.findHistoryByUpdateTime(id, dateStart, dateEnd);
        }
        return historyMapper.systemItemHistoryResponseFromHistories(history);
    }

    private Long calcFolderSize(Item item) {
        return filesMapper.systemItemFromItem(item).getSize();
    }
}
