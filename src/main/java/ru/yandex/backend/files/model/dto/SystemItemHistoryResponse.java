package ru.yandex.backend.files.model.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import java.util.List;

@Data
@AllArgsConstructor
public class SystemItemHistoryResponse {
    private List<SystemItemHistoryUnit> items;
}
