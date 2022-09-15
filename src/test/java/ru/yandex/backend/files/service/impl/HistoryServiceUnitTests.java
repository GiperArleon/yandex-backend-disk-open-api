package ru.yandex.backend.files.service.impl;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.BDDMockito;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Spy;
import org.mockito.junit.jupiter.MockitoExtension;
import ru.yandex.backend.files.mapper.HistoryMapper;
import ru.yandex.backend.files.model.dto.SystemItemHistoryResponse;
import ru.yandex.backend.files.model.dto.SystemItemImport;
import ru.yandex.backend.files.model.dto.SystemItemImportRequest;
import ru.yandex.backend.files.model.entity.History;
import ru.yandex.backend.files.model.entity.Item;
import ru.yandex.backend.files.repository.FilesRepository;
import ru.yandex.backend.files.repository.HistoryRepository;
import ru.yandex.backend.files.validation.FileValidator;
import java.time.ZonedDateTime;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.Mockito.*;
import static org.mockito.Mockito.verify;
import static utils.ItemTestData.FILE_1_1_v0;
import static utils.SystemItemImportTestData.IMPORT_FILE_1_1;

@ExtendWith(MockitoExtension.class)
class HistoryServiceUnitTests {
    private static final String ID = "10001";
    @Mock
    private FilesRepository filesRepository;
    @Mock
    private HistoryRepository historyRepository;
    @Mock
    private HistoryMapper historyMapper;
    @Mock
    private FileValidator fileValidator;
    @Spy
    @InjectMocks
    private HistoryServiceImpl historyService;

    @Test
    void saveFilesHistory_shouldCallRepository() {
        final History history = mock(History.class);
        final SystemItemImport systemItemImport = IMPORT_FILE_1_1;
        ZonedDateTime time = ZonedDateTime.now();
        List<SystemItemImport> items = List.of(systemItemImport);
        final SystemItemImportRequest systemItemImportRequest = new SystemItemImportRequest(items, time);

        BDDMockito
                .given(historyMapper.historyFromSystemItemImport(systemItemImport, time))
                .willReturn(history);
        BDDMockito
                .given(historyRepository.save(history))
                .willReturn(history);

        historyService.saveFilesHistory(systemItemImportRequest);

        verify(historyRepository).save(history);
    }

    @Test
    void getHistory_shouldCall_findById_and_getFoldersHistory() {
        ZonedDateTime dateEnd = ZonedDateTime.now();
        ZonedDateTime dateStart = dateEnd.minusDays(10);
        final Item item = mock(Item.class);
        SystemItemHistoryResponse systemItemHistoryResponse = mock(SystemItemHistoryResponse.class);

        when(filesRepository.findById(ID))
                .thenReturn(Optional.of(item));
        BDDMockito
                .given(fileValidator.checkStartTime(dateStart))
                .willReturn(dateStart);
        BDDMockito
                .given(fileValidator.checkEndTime(dateEnd))
                .willReturn(dateEnd);
        doReturn(systemItemHistoryResponse)
                .when(historyService).getFoldersHistory(ID, dateStart, dateEnd);

        final SystemItemHistoryResponse actual = historyService.getHistory(ID, dateStart, dateEnd);

        assertNotNull(actual);
        assertEquals(systemItemHistoryResponse, actual);
        verify(filesRepository).findById(ID);
        verify(historyService).getFoldersHistory(ID, dateStart, dateEnd);
    }

    @Test
    void getHistory_shouldCall_findById_and_getFilesHistory() {
        ZonedDateTime dateEnd = ZonedDateTime.now();
        ZonedDateTime dateStart = dateEnd.minusDays(10);
        SystemItemHistoryResponse systemItemHistoryResponse = mock(SystemItemHistoryResponse.class);

        when(filesRepository.findById(ID))
                .thenReturn(Optional.of(FILE_1_1_v0));
        BDDMockito
                .given(fileValidator.checkStartTime(dateStart))
                .willReturn(dateStart);
        BDDMockito
                .given(fileValidator.checkEndTime(dateEnd))
                .willReturn(dateEnd);
        doReturn(systemItemHistoryResponse)
                .when(historyService).getFilesHistory(ID, dateStart, dateEnd);

        final SystemItemHistoryResponse actual = historyService.getHistory(ID, dateStart, dateEnd);

        assertNotNull(actual);
        assertEquals(systemItemHistoryResponse, actual);
        verify(filesRepository).findById(ID);
        verify(historyService).getFilesHistory(ID, dateStart, dateEnd);
    }

    @Test
    void getFilesHistory_shouldCallRepository() {
        ZonedDateTime dateEnd = ZonedDateTime.now();
        ZonedDateTime dateStart = dateEnd.minusDays(10);
        final History history = mock(History.class);
        List<History> histories = List.of(history);
        SystemItemHistoryResponse systemItemHistoryResponse = mock(SystemItemHistoryResponse.class);

        when(historyRepository.findHistoryByUpdateTime(ID, dateStart, dateEnd))
                .thenReturn(histories);
        BDDMockito
                .given(historyMapper.systemItemHistoryResponseFromHistories(histories))
                .willReturn(systemItemHistoryResponse);

        final SystemItemHistoryResponse actual = historyService.getFilesHistory(ID, dateStart, dateEnd);

        assertNotNull(actual);
        assertEquals(systemItemHistoryResponse, actual);
        verify(historyRepository).findHistoryByUpdateTime(ID, dateStart, dateEnd);
    }

    @Test
    void getFoldersHistory_shouldCallRepository() {
        ZonedDateTime dateEnd = ZonedDateTime.now();
        ZonedDateTime dateStart = dateEnd.minusDays(10);
        List<History> histories = List.of();
        SystemItemHistoryResponse systemItemHistoryResponse = new SystemItemHistoryResponse(List.of());

        when(historyRepository.findRecursiveHistory(ID))
                .thenReturn(histories);

        final SystemItemHistoryResponse actual = historyService.getFoldersHistory(ID, dateStart, dateEnd);

        assertNotNull(actual);
        assertEquals(systemItemHistoryResponse, actual);
        verify(historyRepository).findRecursiveHistory(ID);
    }
}