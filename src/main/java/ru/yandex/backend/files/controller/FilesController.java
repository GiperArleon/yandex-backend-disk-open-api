package ru.yandex.backend.files.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.yandex.backend.files.model.dto.GeneralResponse;
import ru.yandex.backend.files.model.dto.SystemItem;
import ru.yandex.backend.files.model.dto.SystemItemHistoryResponse;
import ru.yandex.backend.files.model.dto.SystemItemImportRequest;
import ru.yandex.backend.files.service.FilesService;
import ru.yandex.backend.files.service.HistoryService;
import java.time.ZonedDateTime;

@RestController
@RequiredArgsConstructor
public class FilesController {
    private final FilesService filesService;
    private final HistoryService historyService;

    @PostMapping("/imports")
    public ResponseEntity<GeneralResponse> importProducts(@RequestBody SystemItemImportRequest systemItemImportRequest) {
        filesService.saveFiles(systemItemImportRequest);
        historyService.saveFilesHistory(systemItemImportRequest);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @DeleteMapping("/delete/{id}")
    public ResponseEntity<GeneralResponse> deleteProduct(@PathVariable("id") String id) {
        filesService.deleteFileById(id);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @GetMapping("/nodes/{id}")
    public ResponseEntity<SystemItem> findProduct(@PathVariable("id") String id) {
        return ResponseEntity.ok(filesService.getFileById(id));
    }

    @GetMapping("/updates")
    public ResponseEntity<SystemItemHistoryResponse> findUpdatesByDate(@RequestParam(name="date", required = false)
             @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) ZonedDateTime date) {
        return ResponseEntity.ok(filesService.findUpdatesByDate(date));
    }

    @GetMapping("/node/{id}/history")
    public ResponseEntity<SystemItemHistoryResponse> getUpdatedHistory(@PathVariable("id") String id,
             @RequestParam(name="dateStart", required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) ZonedDateTime dateStart,
             @RequestParam(name="dateEnd", required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) ZonedDateTime dateEnd) {
        SystemItemHistoryResponse systemItemHistoryResponse = historyService.getHistory(id, dateStart, dateEnd);
        return ResponseEntity.ok(systemItemHistoryResponse);
    }
}
