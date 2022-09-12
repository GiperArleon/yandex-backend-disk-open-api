package ru.yandex.backend.files.model.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.AllArgsConstructor;
import lombok.Data;
import ru.yandex.backend.files.model.SystemItemType;
import java.time.ZonedDateTime;
import java.util.List;

@Data
@AllArgsConstructor
public class SystemItem {
    private String id;

    private String url;

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd'T'HH:mm:SS'Z'")
    private ZonedDateTime date;

    private String parentId;

    private SystemItemType type;

    private Long size;

    private List<SystemItem> children;
}
