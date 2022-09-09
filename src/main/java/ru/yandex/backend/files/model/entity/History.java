package ru.yandex.backend.files.model.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import ru.yandex.backend.files.model.SystemItemType;
import javax.persistence.*;
import java.time.ZonedDateTime;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "history")
@IdClass(CompositeId.class)
public class History {
    @Id
    @Column(name = "id")
    private String itemId;

    @Column(name = "file_url")
    private String url;

    @Column(name = "update_time")
    private ZonedDateTime updateTime;

    @Column(name = "parent_id")
    private String parentId;

    @Column(name = "item_type")
    @Enumerated(EnumType.STRING)
    private SystemItemType itemType;

    @Column(name = "item_size")
    private Long itemSize;
}
