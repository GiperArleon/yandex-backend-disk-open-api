package ru.yandex.backend.files.repository;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.ActiveProfiles;
import ru.yandex.backend.files.model.entity.History;
import java.util.List;
import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static utils.HistoryTestData.*;

@DataJpaTest
@ActiveProfiles("test")
class HistoryRepositoryIntegrationTests {

    @Autowired
    private HistoryRepository historyRepository;

    @Test
    void findHistoryByUpdateTime() {
        List<History> expected = List.of(FILE_1_1_HISTORY_v1, FILE_1_1_HISTORY_v0);
        History resultRootItem = historyRepository.save(FILE_1_1_HISTORY_v0);
        assertThat(resultRootItem).isNotNull();
        History resultItem = historyRepository.save(FILE_1_1_HISTORY_v1);
        assertThat(resultItem).isNotNull();

        List<History> actual = historyRepository.findHistoryByUpdateTime(FILE_1_1_HISTORY_v0.getItemId(), FILE_1_1_HISTORY_v0.getUpdateTime(), FILE_1_1_HISTORY_v0.getUpdateTime().plusDays(2L));
        assertEquals(expected, actual);
    }

    @Test
    void save_shouldSaveAllItems() {
        List<History> expected = List.of(FILE_1_1_HISTORY_v0);
        History resultRootItem = historyRepository.save(FILE_1_1_HISTORY_v0);
        assertThat(resultRootItem).isNotNull();

        List<History> actual = historyRepository.findHistoryByUpdateTime(FILE_1_1_HISTORY_v0.getItemId(), FILE_1_1_HISTORY_v0.getUpdateTime(), FILE_1_1_HISTORY_v0.getUpdateTime().plusDays(1L));
        assertEquals(expected, actual);
    }
}