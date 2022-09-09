package ru.yandex.backend.files.mapper;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import ru.yandex.backend.files.model.SystemItemType;
import ru.yandex.backend.files.model.dto.*;
import ru.yandex.backend.files.model.entity.Item;
import ru.yandex.backend.files.validation.FileValidator;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.ZonedDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Component
@RequiredArgsConstructor
public class FilesMapper extends RootMapper {

    private final FileValidator fileValidator;

    public Item itemFromSystemItemImport(SystemItemImport systemItemImport, ZonedDateTime updateTime) {
        return new Item(
                systemItemImport.getId(),
                systemItemImport.getUrl(),
                updateTime,
                systemItemImport.getParentId(),
                systemItemImport.getType(),
                systemItemImport.getType()==SystemItemType.FOLDER? null:systemItemImport.getSize(),
                null
        );
    }

    public List<Item> itemsFromSystemItemImportRequest(SystemItemImportRequest systemItemImportRequest) {
        return systemItemImportRequest
                .getItems()
                .stream()
                .peek(fileValidator::validateSystemItemImport)
                .map(v -> itemFromSystemItemImport(v, systemItemImportRequest.getUpdateDate()))
                .collect(Collectors.toList());
    }

    public SystemItem systemItemFromItem(Item item) {
        formatItem(item);
        return new SystemItem(
                item.getItemId(),
                item.getUrl(),
                formatToUTC(item.getUpdateTime()),
                item.getParentId(),
                item.getItemType(),
                item.getItemSize(),
                systemItemsFromItems(item.getItemType(), item.getChildrens()));
    }

    public List<SystemItem> systemItemsFromItems(SystemItemType itemType, List<Item> items) {
        if(itemType == SystemItemType.FILE && items.isEmpty())
            return null;
        return items.stream()
                .map(this::systemItemFromItem)
                .collect(Collectors.toList());
    }

    public Long roundSize(Double amount) {
        BigDecimal bigDecimal = new BigDecimal(amount).setScale(0, RoundingMode.DOWN);
        return bigDecimal.longValue();
    }

    public void sizeCalc(Item item) {
        if(item.getItemType() == SystemItemType.FOLDER) {
            Long size = item.getFlatChildrens()
                    .filter(v -> v.getItemType() == SystemItemType.FILE)
                    .mapToLong(Item::getItemSize)
                    .sum();
            item.setItemSize(size);
        }
    }

    public Item formatItem(Item item) {
        sizeCalc(item);
        ZonedDateTime oldestDate = item.getChildrens().stream()
                .map(this::formatItem)
                .map(Item::getUpdateTime)
                .max(ZonedDateTime::compareTo)
                .orElse(item.getUpdateTime());
        item.setUpdateTime(oldestDate);
        return item;
    }

    public SystemItemHistoryUnit systemItemHistoryUnitFromItem(Item item) {
        return new SystemItemHistoryUnit(
                item.getItemId(),
                item.getUrl(),
                item.getParentId(),
                item.getItemType(),
                item.getItemSize(),
                formatToUTC(item.getUpdateTime())
        );
    }

    public SystemItemHistoryResponse systemItemHistoryResponseFromItems(List<Item> items) {
        List<SystemItemHistoryUnit> itemsRes = items.stream()
                .map(this::systemItemHistoryUnitFromItem)
                .collect(Collectors.toList());
        return new SystemItemHistoryResponse(itemsRes);
    }
}
