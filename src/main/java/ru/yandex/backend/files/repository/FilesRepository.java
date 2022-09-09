package ru.yandex.backend.files.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import ru.yandex.backend.files.model.entity.Item;
import java.time.ZonedDateTime;
import java.util.List;

@Repository
public interface FilesRepository extends JpaRepository<Item, String> {
    @Query(value = "select id, file_url, update_time, parent_id, item_type, item_size " +
            "from items v " +
            "where v.item_type = 'FILE'" +
            "and v.update_time >= :from " +
            "and v.update_time <= :to",
            nativeQuery = true)
    List<Item> findFilesByUpdateTime(@Param("from") ZonedDateTime from, @Param("to") ZonedDateTime to);
}
