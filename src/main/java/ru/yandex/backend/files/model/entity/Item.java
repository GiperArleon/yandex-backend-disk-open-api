package ru.yandex.backend.files.model.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import ru.yandex.backend.files.model.SystemItemType;
import javax.persistence.*;
import java.time.ZonedDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Stream;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "items")
public class Item {
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

    @OneToMany(fetch = FetchType.EAGER,
            cascade = {CascadeType.REMOVE})
    @JoinColumn(name = "parent_id",
            referencedColumnName = "id", updatable = false)
    private List<Item> childrens;

    public Stream<Item> getFlatChildrens() {
        return Stream.concat(
                Stream.of(this),
                childrens.stream().flatMap(Item::getFlatChildrens));
    }
}
