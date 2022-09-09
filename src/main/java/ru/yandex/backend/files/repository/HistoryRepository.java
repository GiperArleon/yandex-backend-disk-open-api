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
    @Query(value = "select id, file_url, update_time, parent_id, item_type, item_size " +
            "from history v " +
            "where v.id = :id " +
            "and v.update_time >= :from " +
            "and v.update_time <= :to ORDER BY v.update_time DESC",
            nativeQuery = true)
    List<History> findHistoryByUpdateTime(@Param("id") String id, @Param("from") ZonedDateTime from, @Param("to") ZonedDateTime to);

    @Query(value = "select id, file_url, update_time, parent_id, item_type, item_size " +
            "from history v " +
            "where v.id = :id ORDER BY v.update_time DESC",
            nativeQuery = true)
    List<History> findFullHistory(@Param("id") String id);
}