package ru.yandex.backend.files.controller;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import ru.yandex.backend.files.model.dto.SystemItem;
import ru.yandex.backend.files.model.dto.SystemItemHistoryResponse;
import ru.yandex.backend.files.model.dto.SystemItemImportRequest;
import ru.yandex.backend.files.service.FilesService;
import ru.yandex.backend.files.service.HistoryService;
import java.time.ZonedDateTime;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.Mockito.*;
import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
class FilesControllerTest {
    private final static String ID = "1000";
    @Mock
    private HistoryService historyService;
    @Mock
    private FilesService filesService;
    @InjectMocks
    private FilesController controller;

    @Test
    void importFiles_shouldCallFileService_HistoryService() {
        final SystemItemImportRequest systemItemImportRequest = mock(SystemItemImportRequest.class);
        ResponseEntity<Void> itemResponseEntity = new ResponseEntity<>(HttpStatus.OK);

        Mockito
                .doNothing()
                .when(filesService).saveFiles(systemItemImportRequest);
        Mockito
                .doNothing()
                .when(historyService).saveFilesHistory(systemItemImportRequest);

        final ResponseEntity<Void> actual = controller.importFiles(systemItemImportRequest);

        assertEquals(itemResponseEntity, actual);
        verify(filesService).saveFiles(systemItemImportRequest);
        verify(historyService).saveFilesHistory(systemItemImportRequest);
    }

    @Test
    void deleteFiles_shouldCallFileService() {
        ResponseEntity<Void> itemResponseEntity = new ResponseEntity<>(HttpStatus.OK);

        Mockito
                .doNothing()
                .when(filesService).deleteFileById(ID);

        final ResponseEntity<Void> actual = controller.deleteFile(ID);

        assertEquals(itemResponseEntity, actual);
        verify(filesService).deleteFileById(ID);
    }

    @Test
    void findFile_shouldCallFileService() {
        final SystemItem item = mock(SystemItem.class);
        ResponseEntity<SystemItem> itemResponseEntity = ResponseEntity.ok(item);

        when(filesService.getFileById(ID)).thenReturn(item);

        final ResponseEntity<SystemItem> actual = controller.findFile(ID);

        assertNotNull(actual);
        assertEquals(itemResponseEntity, actual);
        verify(filesService).getFileById(ID);
    }

    @Test
    void findUpdatesByDate_shouldCallFileService() {
        ZonedDateTime date = ZonedDateTime.now();
        SystemItemHistoryResponse systemItemHistoryResponse = mock(SystemItemHistoryResponse.class);
        ResponseEntity<SystemItemHistoryResponse> itemResponseEntity = ResponseEntity.ok(systemItemHistoryResponse);

        when(filesService.findUpdatesByDate(date)).thenReturn(systemItemHistoryResponse);

        ResponseEntity<SystemItemHistoryResponse> actual = controller.findUpdatesByDate(date);

        assertNotNull(actual);
        assertEquals(itemResponseEntity, actual);
        verify(filesService).findUpdatesByDate(date);
    }

    @Test
    void getUpdatedHistory_shouldCallHistoryService() {
        ZonedDateTime to = ZonedDateTime.now();
        ZonedDateTime from = to.minusDays(10);
        SystemItemHistoryResponse systemItemHistoryResponse = mock(SystemItemHistoryResponse.class);
        ResponseEntity<SystemItemHistoryResponse> itemResponseEntity = ResponseEntity.ok(systemItemHistoryResponse);

        when(historyService.getHistory(ID, from, to)).thenReturn(systemItemHistoryResponse);

        ResponseEntity<SystemItemHistoryResponse> actual = controller.getUpdatedHistory(ID, from, to);

        assertNotNull(actual);
        assertEquals(itemResponseEntity, actual);
        verify(historyService).getHistory(ID, from, to);
    }
}