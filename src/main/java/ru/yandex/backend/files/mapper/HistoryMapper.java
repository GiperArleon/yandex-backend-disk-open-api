package ru.yandex.backend.files.mapper;

import org.springframework.stereotype.Component;
import ru.yandex.backend.files.model.dto.SystemItemHistoryResponse;
import ru.yandex.backend.files.model.dto.SystemItemHistoryUnit;
import ru.yandex.backend.files.model.dto.SystemItemImport;
import ru.yandex.backend.files.model.dto.SystemItemImportRequest;
import ru.yandex.backend.files.model.entity.History;
import ru.yandex.backend.files.model.entity.Item;

import java.time.ZonedDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Component
public class HistoryMapper extends RootMapper {

    public History historyFromItem(Item item) {
        return new History(
                item.getItemId(),
                item.getUrl(),
                item.getUpdateTime(),
                item.getParentId(),
                item.getItemType(),
                item.getItemSize()
        );
    }

    public History historyFromSystemItemImport(SystemItemImport systemItemImport,  ZonedDateTime updateTime) {
        return new History(
                systemItemImport.getId(),
                systemItemImport.getUrl(),
                updateTime,
                systemItemImport.getParentId(),
                systemItemImport.getType(),
                systemItemImport.getSize()
        );
    }

    public SystemItemHistoryUnit systemItemHistoryUnitFromHistory(History history) {
        return new SystemItemHistoryUnit(
                history.getItemId(),
                history.getUrl(),
                history.getParentId(),
                history.getItemType(),
                history.getItemSize(),
                formatToUTC(history.getUpdateTime())
        );
    }

    public SystemItemHistoryResponse systemItemHistoryResponseFromHistories(List<History> histories) {
        List<SystemItemHistoryUnit> itemsRes = histories.stream()
                .map(this::systemItemHistoryUnitFromHistory)
                .collect(Collectors.toList());
        return new SystemItemHistoryResponse(itemsRes);
    }
}
