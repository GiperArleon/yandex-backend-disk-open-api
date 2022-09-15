package utils;

import ru.yandex.backend.files.model.SystemItemType;
import ru.yandex.backend.files.model.entity.History;
import java.time.ZonedDateTime;
import static utils.TestDataProvider.createHistory;

public class HistoryTestData {
    public static final History ROOT_FOLDER_FLAT_HISTORY = createHistory(
            "069cb8d7-bbdd-47d3-ad8f-82ef4c269df1",
            null,
            ZonedDateTime.parse("2022-02-01T15:00+03:00[Europe/Moscow]"),
            null,
            SystemItemType.FOLDER,
            0L
    );

    public static final History FIRST_FOLDER_FLAT_HISTORY = createHistory(
            "d515e43f-f3f6-4471-bb77-6b455017a2d2",
            "/file/url1",
            ZonedDateTime.parse("2022-02-02T15:00+03:00[Europe/Moscow]"),
            "069cb8d7-bbdd-47d3-ad8f-82ef4c269df1",
            SystemItemType.FOLDER,
            0L
    );

    public static final History FILE_1_1_HISTORY_v0 = createHistory(
            "863e1a7a-1304-42ae-943b-179184c077e3",
            "/file/url1",
            ZonedDateTime.parse("2022-02-03T15:00+03:00[Europe/Moscow]"),
            "069cb8d7-bbdd-47d3-ad8f-82ef4c269df1",
            SystemItemType.FILE,
            128L);

    public static final History FILE_1_1_HISTORY_v1 = createHistory(
            "863e1a7a-1304-42ae-943b-179184c077e3",
            "/file/url1",
            ZonedDateTime.parse("2022-02-04T15:00+03:00[Europe/Moscow]"),
            "069cb8d7-bbdd-47d3-ad8f-82ef4c269df1",
            SystemItemType.FILE,
            228L);

    public static final History FILE_1_2_HISTORY_v0  = createHistory(
            "b1d8fd7d-2ae3-47d5-b2f9-0f094af800d4",
            "/file/url2",
            ZonedDateTime.parse("2022-02-02T15:00+03:00[Europe/Moscow]"),
            "069cb8d7-bbdd-47d3-ad8f-82ef4c269df1",
            SystemItemType.FILE,
            256L);
}
