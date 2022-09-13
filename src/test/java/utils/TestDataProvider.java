package utils;

import ru.yandex.backend.files.model.SystemItemType;
import ru.yandex.backend.files.model.dto.SystemItem;
import ru.yandex.backend.files.model.dto.SystemItemImport;
import ru.yandex.backend.files.model.entity.Item;

import java.time.ZonedDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class TestDataProvider {

    public static Item createItem(
            String itemId,
            String itemUrl,
            ZonedDateTime updateTime,
            String parentId,
            SystemItemType itemType,
            Long itemSize,
            List<Item> childrens) {
        return new Item(
                itemId == null ? UUID.randomUUID().toString() : itemId,
                itemUrl,
                updateTime,
                parentId,
                itemType,
                itemSize,
                childrens
        );
    }

    public static Item createItem(
            String itemId,
            String itemUrl,
            ZonedDateTime updateTime,
            String parentId,
            SystemItemType itemType,
            Long itemSize) {
        return new Item(
                itemId == null ? UUID.randomUUID().toString() : itemId,
                itemUrl,
                updateTime,
                parentId,
                itemType,
                itemSize,
                new ArrayList<>()
        );
    }

    public static SystemItem createSystemItem(
            String itemId,
            String itemUrl,
            ZonedDateTime updateTime,
            String parentId,
            SystemItemType itemType,
            Long itemSize,
            List<SystemItem> childrens) {
        return new SystemItem(
                itemId == null ? UUID.randomUUID().toString() : itemId,
                itemUrl,
                updateTime,
                parentId,
                itemType,
                itemSize,
                childrens
        );
    }

    public static SystemItem createSystemItemEmptyChildrens(
            String itemId,
            String itemUrl,
            ZonedDateTime updateTime,
            String parentId,
            SystemItemType itemType,
            Long itemSize) {
        return new SystemItem(
                itemId == null ? UUID.randomUUID().toString() : itemId,
                itemUrl,
                updateTime,
                parentId,
                itemType,
                itemSize,
                new ArrayList<>()
        );
    }

    public static SystemItemImport createSystemItemImport(
            String itemId,
            String itemUrl,
            String parentId,
            SystemItemType itemType,
            Long itemSize) {
        return new SystemItemImport(
                itemId == null ? UUID.randomUUID().toString() : itemId,
                itemUrl,
                parentId,
                itemType,
                itemSize
        );
    }
}
