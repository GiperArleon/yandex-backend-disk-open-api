package ru.yandex.backend.files.repository;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import ru.yandex.backend.files.model.entity.Item;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;
import static utils.ItemTestData.*;

@DataJpaTest
@ActiveProfiles("test")
@Transactional(propagation = Propagation.NOT_SUPPORTED)
class FilesRepositoryIntegrationTests {
    private static final String ID = "10001";

    @Autowired
    private FilesRepository filesRepository;

    @Test
    void findFilesByUpdateTime() {
        List<Item> expected = List.of(FILE_1_1_v0, FILE_1_2_v0);

        Item resultRootItem = filesRepository.save(ROOT_FOLDER_FLAT);
        assertThat(resultRootItem).isNotNull();
        Item resultItem = filesRepository.save(FIRST_FOLDER_FLAT);
        assertThat(resultItem).isNotNull();
        Item resultFileOne = filesRepository.save(FILE_1_1_v0);
        assertThat(resultFileOne).isNotNull();
        Item resultFileTwo = filesRepository.save(FILE_1_2_v0);
        assertThat(resultFileTwo).isNotNull();

        List<Item> actual  = filesRepository.findFilesByUpdateTime(FILE_1_1_v0.getUpdateTime().minusDays(1), FILE_1_1_v0.getUpdateTime());

        assertEquals(expected, actual);
        filesRepository.deleteById(ROOT_FOLDER_FLAT.getItemId());
    }

    @Test
    void findById_itShouldFindItemById() {
        Item resultItem = filesRepository.save(ROOT_FOLDER_FLAT);
        assertThat(resultItem).isNotNull();

        Optional<Item> optionalItem = filesRepository.findById(ROOT_FOLDER_FLAT.getItemId());
        assertThat(optionalItem)
                .isPresent()
                .hasValueSatisfying(c -> assertThat(c).usingRecursiveComparison()
                        .isEqualTo(ROOT_FOLDER_FLAT));

        filesRepository.deleteById(ROOT_FOLDER_FLAT.getItemId());
    }

    @Test
    void save_shouldSaveLastCalledItem_whenCalledMultipleTimes() {
        Item resultRootItem = filesRepository.save(ROOT_FOLDER_FLAT);
        assertThat(resultRootItem).isNotNull();
        Item resultItem = filesRepository.save(FIRST_FOLDER_FLAT);
        assertThat(resultItem).isNotNull();
        Item resultFileOne = filesRepository.save(FILE_1_1_v0);
        assertThat(resultFileOne).isNotNull();
        Item resultFileTwo = filesRepository.save(FILE_1_1_v1);
        assertThat(resultFileTwo).isNotNull();

        Optional<Item> actual = filesRepository.findById(FILE_1_1_v1.getItemId());
        assertThat(actual)
                .isPresent()
                .hasValueSatisfying(c -> assertThat(c).usingRecursiveComparison()
                        .isEqualTo(FILE_1_1_v1));

        filesRepository.deleteById(ROOT_FOLDER_FLAT.getItemId());
    }

    @Test
    void deleteById_shouldNoThrow() {
        Item resultRootItem = filesRepository.save(ROOT_FOLDER_FLAT);
        assertThat(resultRootItem).isNotNull();

        assertDoesNotThrow(() ->filesRepository.deleteById(ROOT_FOLDER_FLAT.getItemId()));
    }

    @Test
    void deleteById_shouldThrowExeption() {
        Exception exception = assertThrows(EmptyResultDataAccessException.class, () ->filesRepository.deleteById(ID));
        String expectedMessage = "No class ru.yandex.backend.files.model.entity.Item entity with id " + ID + " exists!";
        String actualMessage = exception.getMessage();
        assertEquals(expectedMessage, actualMessage);
    }
}