package webserver.http;

import java.util.HashMap;
import java.util.Map;

public class MultipartParser {

    private MultipartParser() {}

    public static MultipartResult parse(HttpRequest request) {
        String contentType = request.getHeader("content-type");

        if (contentType == null || !contentType.startsWith("multipart/form-data")) {
            throw new IllegalStateException("Request is not multipart/form-data");
        }

        String boundary = contentType.split("boundary=")[1];

        byte[] bodyBytes = request.getBody();
        String body = new String(bodyBytes);

        String[] parts = body.split("--" + boundary);

        Map<String, String> fields = new HashMap<>();
        Map<String, UploadFile> files = new HashMap<>();

        // TODO: for문돌면서 파싱 로직 작성하기

        return new MultipartResult(fields, files);
    }
}
