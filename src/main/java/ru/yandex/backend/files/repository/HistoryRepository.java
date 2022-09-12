package ru.yandex.backend.files.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import ru.yandex.backend.files.model.entity.CompositeId;
import ru.yandex.backend.files.model.entity.History;
import java.time.ZonedDateTime;
import java.util.List;

@Repository
public interface HistoryRepository extends JpaRepository<History, CompositeId> {
    @Query(value = "SELECT id, file_url, update_time, parent_id, item_type, item_size " +
            "FROM history v " +
            "WHERE v.id = :id " +
            "AND v.update_time >= :from " +
            "AND v.update_time <= :to ORDER BY v.update_time DESC",
            nativeQuery = true)
    List<History> findHistoryByUpdateTime(@Param("id") String id, @Param("from") ZonedDateTime from, @Param("to") ZonedDateTime to);

    @Query(value = "WITH RECURSIVE r AS " +
            "(SELECT i.id, i.file_url, i.update_time, i.parent_id, i.item_type, i.item_size, 1 AS level FROM items i WHERE i.id = :id " +
            "UNION " +
            "SELECT u.id, u.file_url, u.update_time, u.parent_id, u.item_type, u.item_size, r.level + 1 AS level FROM items u " +
            "JOIN r ON u.parent_id = r.id) " +
            "SELECT id AS id, file_url AS file_url, update_time AS update_time, parent_id AS parent_id, item_size AS item_size, item_type AS item_type FROM history " +
            "WHERE id IN (SELECT DISTINCT id FROM r) or parent_id IN (SELECT distinct id FROM r) " +
            "UNION " +
            "SELECT id AS id, file_url AS file_url, update_time AS update_time, parent_id AS parent_id, item_size AS item_size, item_type AS item_type FROM items " +
            "WHERE id IN (SELECT DISTINCT id FROM r)", nativeQuery = true)
    List<History> findRecursiveHistory(@Param("id") String id);
}