package utils;

import ru.yandex.backend.files.model.SystemItemType;
import ru.yandex.backend.files.model.dto.SystemItemImport;
import static utils.TestDataProvider.createSystemItemImport;

public class SystemItemImportTestData {
    public static final SystemItemImport IMPORT_ROOT_FOLDER_FLAT = createSystemItemImport(
            "069cb8d7-bbdd-47d3-ad8f-82ef4c269df1",
            null,
            null,
            SystemItemType.FOLDER,
            0L
    );

    public static final SystemItemImport IMPORT_FILE_1_1 = createSystemItemImport(
            "069cb8d7-bbdd-47d3-ad8f-82ef4c269df1",
            "/file/url1",
            null,
            SystemItemType.FILE,
            100L
    );
}
