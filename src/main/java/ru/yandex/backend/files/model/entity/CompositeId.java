package ru.yandex.backend.files.model.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.io.Serializable;
import java.time.ZonedDateTime;
import java.util.UUID;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class CompositeId implements Serializable {
    private UUID itemId;
    private ZonedDateTime updateTime;
}
