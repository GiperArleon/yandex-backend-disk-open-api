package ru.yandex.backend.files.service.impl;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.BDDMockito;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import ru.yandex.backend.files.mapper.FilesMapper;
import ru.yandex.backend.files.model.dto.SystemItem;
import ru.yandex.backend.files.model.dto.SystemItemHistoryResponse;
import ru.yandex.backend.files.model.dto.SystemItemImportRequest;
import ru.yandex.backend.files.model.entity.Item;
import ru.yandex.backend.files.repository.FilesRepository;
import ru.yandex.backend.files.validation.FileValidator;
import java.time.ZonedDateTime;
import java.util.List;
import java.util.Optional;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class FilesServiceUnitTests {
    private static final String ID = "10001";

    @Mock
    private FilesRepository filesRepository;
    @Mock
    private FilesMapper filesMapper;
    @Mock
    private FileValidator fileValidator;
    @InjectMocks
    private FilesServiceImpl filesService;

    @Test
    void getFileById_shouldCallRepository() {
        final Item item = mock(Item.class);
        final SystemItem systemItem = mock(SystemItem.class);

        when(filesRepository.findById(ID))
          .thenReturn(Optional.ofNullable(item));
        BDDMockito
          .given(filesMapper.systemItemFromItem(item))
          .willReturn(systemItem);

        final SystemItem actual = filesService.getFileById(ID);

        assertNotNull(actual);
        assertEquals(systemItem, actual);
        verify(filesRepository).findById(ID);
    }

    @Test
    void saveFiles_shouldCallRepository() {
        final Item item = mock(Item.class);
        final SystemItemImportRequest systemItemImportRequest = mock(SystemItemImportRequest.class);

        Mockito
           .doNothing()
           .when(fileValidator).validateSystemItemImportRequest(systemItemImportRequest);
        BDDMockito
           .given(filesMapper.itemsFromSystemItemImportRequest(systemItemImportRequest))
           .willReturn(List.of(item));
        BDDMockito
           .given(filesRepository.save(item))
           .willReturn(item);

        filesService.saveFiles(systemItemImportRequest);

        verify(filesRepository).save(item);
    }

    @Test
    void deleteFileById_shouldCallRepository() {
        final Item item = mock(Item.class);
        final SystemItem systemItem = mock(SystemItem.class);

        when(filesRepository.findById(ID))
           .thenReturn(Optional.ofNullable(item));
        Mockito
           .doNothing()
           .when(fileValidator).validateNotNull(ID);
        BDDMockito
            .given(filesMapper.systemItemFromItem(item))
            .willReturn(systemItem);
        Mockito
            .doNothing()
            .when(filesRepository).deleteById(ID);

        filesService.deleteFileById(ID);

        verify(filesRepository).deleteById(ID);
    }

    @Test
    void findUpdatesByDate_shouldCallRepository() {
        ZonedDateTime to = ZonedDateTime.now();
        ZonedDateTime from = to.minusDays(10);
        final Item item = mock(Item.class);
        List<Item> items = List.of(item);
        SystemItemHistoryResponse systemItemHistoryResponse = mock(SystemItemHistoryResponse.class);

        when(filesRepository.findFilesByUpdateTime(from, to))
             .thenReturn(items);
        BDDMockito
            .given(fileValidator.makeFromTime(to))
            .willReturn(from);
        BDDMockito
             .given(fileValidator.checkToTime(to))
             .willReturn(to);
        BDDMockito
             .given(filesMapper.systemItemHistoryResponseFromItems(items))
             .willReturn(systemItemHistoryResponse);


        final SystemItemHistoryResponse actual = filesService.findUpdatesByDate(to);

        assertNotNull(actual);
        assertEquals(systemItemHistoryResponse, actual);
        verify(filesRepository).findFilesByUpdateTime(from, to);
    }
}