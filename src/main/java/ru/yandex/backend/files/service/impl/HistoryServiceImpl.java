package ru.yandex.backend.files.service.impl;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.yandex.backend.files.exceptions.ObjectNotFoundException;
import ru.yandex.backend.files.mapper.HistoryMapper;
import ru.yandex.backend.files.model.SystemItemType;
import ru.yandex.backend.files.model.dto.SystemItemHistoryResponse;
import ru.yandex.backend.files.model.dto.SystemItemHistoryUnit;
import ru.yandex.backend.files.model.dto.SystemItemImportRequest;
import ru.yandex.backend.files.model.entity.History;
import ru.yandex.backend.files.model.entity.Item;
import ru.yandex.backend.files.repository.FilesRepository;
import ru.yandex.backend.files.repository.HistoryRepository;
import ru.yandex.backend.files.service.HistoryService;
import ru.yandex.backend.files.validation.FileValidator;

import java.time.*;
import java.util.*;
import java.util.function.BinaryOperator;
import java.util.function.Function;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class HistoryServiceImpl implements HistoryService {
    private final FilesRepository productsRepository;
    private final HistoryRepository historyRepository;
    private final HistoryMapper historyMapper;
    private final FileValidator fileValidator;

    @Override
    public void saveFilesHistory(SystemItemImportRequest systemItemImportRequest) {
        systemItemImportRequest.getItems().forEach(v -> {
            if(v.getType() == SystemItemType.FILE) {
                historyRepository.save(historyMapper.historyFromSystemItemImport(v, systemItemImportRequest.getUpdateDate()));
            }
        });
    }

    @Override
    public SystemItemHistoryResponse getHistory(String id, ZonedDateTime dateStart, ZonedDateTime dateEnd) {
        Item item = productsRepository.findById(id).orElseThrow(() -> new ObjectNotFoundException("no history"));
        if(item.getItemType() == SystemItemType.FILE) {
            return getFilesHistory(id, dateStart, dateEnd);
        }
        return getFoldersHistory(id, dateStart, dateEnd);
    }

    private SystemItemHistoryResponse getFilesHistory(String id, ZonedDateTime dateStart, ZonedDateTime dateEnd) {
        List<History> history = historyRepository.findHistoryByUpdateTime(id, fileValidator.checkStartTime(dateStart), fileValidator.checkEndTime(dateEnd));
        return historyMapper.systemItemHistoryResponseFromHistories(history);
    }

    private SystemItemHistoryResponse getFoldersHistory(String id, ZonedDateTime dateStart, ZonedDateTime dateEnd) {
        List<History> historyPoints = historyRepository.findRecursiveHistory(id);
        //попробуем обнулить папки
        historyPoints.forEach(x -> {
            if(x.getItemType() == SystemItemType.FOLDER)
                x.setItemSize(null);
        });

        Set<ZonedDateTime> unicHistoryPointDates = historyPoints.stream()
                .map(History::getUpdateTime)
                .collect(Collectors.toCollection(
                        () -> new TreeSet<ZonedDateTime>(Comparator.reverseOrder())
                ));

        List<SystemItemHistoryUnit> response = new ArrayList<>(unicHistoryPointDates.size());

        for(ZonedDateTime date: unicHistoryPointDates) {
            historyPoints.removeIf(e -> e.getItemType() == SystemItemType.FILE &&
                                        date.isBefore(e.getUpdateTime()));

            Map<String, History> groupByHistoryPointDate = historyPoints.stream()
                    .collect(Collectors.toMap(History::getItemId, Function.identity(),
                            BinaryOperator.maxBy(Comparator.comparing(History::getUpdateTime))
                    ));

            Map<String, List<History>> groupByParentId = new HashMap<>();
            for(History history: groupByHistoryPointDate.values()) {
                groupByParentId.computeIfAbsent(history.getParentId(),
                                                v -> new ArrayList<>()).add(history);
            }
            SizeData sizeData = calcSize(id, groupByParentId, null);

            History historyToPointDate = groupByHistoryPointDate.get(id);
            if(historyToPointDate != null) {
                SystemItemHistoryUnit resultUnit = new SystemItemHistoryUnit(historyToPointDate.getItemId(), historyToPointDate.getUrl(),
                        historyToPointDate.getParentId(), historyToPointDate.getItemType(), getResultSize(sizeData), date);
                response.add(resultUnit);
            }
        }

        ZonedDateTime effectivelyFinalStart = fileValidator.checkStartTime(dateStart);
        ZonedDateTime effectivelyFinalEnd = fileValidator.checkEndTime(dateEnd);
        List<SystemItemHistoryUnit> systemItemHistoryUnitList = response.stream()
                .distinct()
                .filter(resultUnit -> resultUnit.getDate().compareTo(effectivelyFinalStart) >= 0 &&
                                      resultUnit.getDate().isBefore(effectivelyFinalEnd))
                .collect(Collectors.toList());
        return new SystemItemHistoryResponse(systemItemHistoryUnitList);
    }

    private SizeData calcSize(String parentId, Map<String, List<History>> groupByParentId, SizeData sizeData) {
        List<History> unitList = groupByParentId.get(parentId);
        if(unitList == null || unitList.isEmpty()) {
            return sizeData;
        }
        for(History unit: unitList) {
            if(SystemItemType.FILE == unit.getItemType()) {
                if(sizeData == null) {
                    sizeData = new SizeData();
                }
                sizeData.plusSize(unit.getItemSize());
            } else {
                sizeData = calcSize(unit.getItemId(), groupByParentId, sizeData);
            }
        }
        return sizeData;
    }

    private Long getResultSize(SizeData sizeData) {
        if(sizeData == null) {
            return null;
        }
        return sizeData.getSize();
    }

    @Getter
    static class SizeData {
        private long size = 0;

        void plusSize(long val) {
            size += val;
        }
    }
}
