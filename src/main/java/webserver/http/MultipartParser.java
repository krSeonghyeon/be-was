package webserver.http;

import java.nio.charset.StandardCharsets;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

public class MultipartParser {

    private MultipartParser() {}

    public static MultipartResult parse(HttpRequest request) {
        String contentType = request.getHeader("content-type");
        if (contentType == null || !contentType.startsWith("multipart/form-data")) {
            throw new IllegalStateException("Request is not multipart/form-data");
        }

        String boundary = contentType.substring(contentType.indexOf("boundary=") + 9);
        byte[] boundaryBytes = ("--" + boundary).getBytes(StandardCharsets.UTF_8);
        byte[] body = request.getBody();

        Map<String, String> fields = new HashMap<>();
        Map<String, UploadFile> files = new HashMap<>();

        int pos = 0;

        while (true) {
            int partStart = indexOf(body, boundaryBytes, pos);
            if (partStart < 0) break;

            partStart += boundaryBytes.length;

            // 마지막 boundary("----boundary--") 체크
            if (body.length > partStart + 2 && body[partStart] == '-' && body[partStart + 1] == '-') {
                break;
            }

            // CRLF 스킵
            if (body.length > partStart + 2 && body[partStart] == '\r' && body[partStart + 1] == '\n') {
                partStart += 2;
            }

            int partEnd = indexOf(body, boundaryBytes, partStart);
            if (partEnd < 0) partEnd = body.length;

            // 헤더/바디 구분 (\r\n\r\n)
            int headerEnd = indexOf(body, "\r\n\r\n".getBytes(StandardCharsets.UTF_8), partStart);
            if (headerEnd < 0) break;

            byte[] headerBytes = Arrays.copyOfRange(body, partStart, headerEnd);

            int bodyStart = headerEnd + 4;
            int bodyEnd = partEnd;

            // boundary 직전 CRLF 제거
            while (bodyEnd > bodyStart && (body[bodyEnd - 1] == '\n' || body[bodyEnd - 1] == '\r')) {
                bodyEnd--;
            }

            byte[] bodyBytes = Arrays.copyOfRange(body, bodyStart, bodyEnd);

            String headerText = new String(headerBytes, StandardCharsets.UTF_8);
            String name = extract(headerText, "name");
            String filename = extract(headerText, "filename");
            String partContentType = null;

            for (String line : headerText.split("\r\n")) {
                if (line.toLowerCase().startsWith("content-type:")) {
                    partContentType = line.split(":")[1].trim();
                }
            }

            if (name == null) {
                pos = partEnd;
                continue;
            }

            if (filename == null) {
                fields.put(name, new String(bodyBytes, StandardCharsets.UTF_8));
            } else {
                files.put(name, new UploadFile(filename, partContentType, bodyBytes));
            }

            pos = partEnd;
        }

        return new MultipartResult(fields, files);
    }

    private static int indexOf(byte[] data, byte[] pattern, int start) {
        outer:
        for (int i = start; i <= data.length - pattern.length; i++) {
            for (int j = 0; j < pattern.length; j++) {
                if (data[i + j] != pattern[j]) {
                    continue outer;
                }
            }
            return i;
        }
        return -1;
    }

    private static String extract(String header, String key) {
        String search = key + "=\"";
        int start = header.indexOf(search);
        if (start < 0) return null;
        start += search.length();
        int end = header.indexOf("\"", start);
        return header.substring(start, end);
    }
}
