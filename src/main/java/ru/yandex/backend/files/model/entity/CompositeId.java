package ru.yandex.backend.files.model.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.io.Serializable;
import java.time.ZonedDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class CompositeId implements Serializable {
    private String itemId;
    private ZonedDateTime updateTime;
}
