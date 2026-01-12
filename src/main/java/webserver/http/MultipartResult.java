package webserver.http;

import java.util.Map;

public record MultipartResult(
        Map<String, String> fields,
        Map<String, UploadFile> files
) {


}
