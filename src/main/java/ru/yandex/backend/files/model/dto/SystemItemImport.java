package ru.yandex.backend.files.model.dto;

import lombok.Data;
import ru.yandex.backend.files.model.SystemItemType;

@Data
public class SystemItemImport {
    private String id;

    private String url;

    private String parentId;

    private SystemItemType type;

    private Long size;
}
