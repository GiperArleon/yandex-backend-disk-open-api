package ru.yandex.backend.files.repository;

import org.springframework.cache.annotation.CacheConfig;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import ru.yandex.backend.files.model.entity.Item;
import java.time.ZonedDateTime;
import java.util.List;
import java.util.Optional;

@Repository
@CacheConfig(cacheNames = "FilesCache")
public interface FilesRepository extends JpaRepository<Item, String> {
    @Cacheable
    @Query(value = "SELECT id, file_url, update_time, parent_id, item_type, item_size " +
            "FROM items v " +
            "WHERE v.item_type = 'FILE'" +
            "AND v.update_time >= :from " +
            "AND v.update_time <= :to",
            nativeQuery = true)
    List<Item> findFilesByUpdateTime(@Param("from") ZonedDateTime from, @Param("to") ZonedDateTime to);

    @Cacheable(key = "#id")
    Optional<Item> findById(String id);

    @CacheEvict(allEntries = true)
    Item save(Item item);

    @CacheEvict(allEntries = true)
    void deleteById(String id);
}
