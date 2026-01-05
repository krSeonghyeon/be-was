package webserver.staticresource;

import webserver.http.ContentType;
import webserver.http.HttpResponse;

import java.io.*;

public class StaticResourceHandler {

    private static final String BASE_PATH = "src/main/resources/static";

    public HttpResponse handle(String path) throws IOException {
        File file = new File(BASE_PATH + path);

        if (file.isDirectory()) {
            file = new File(file, "index.html");
        }

        if (!file.exists()) {
            return HttpResponse.notFound();
        }

        byte[] body = readFileToBytes(file);
        ContentType contentType = ContentType.fromFileName(file.getName());
        return HttpResponse.ok(body, contentType.getMimeType());
    }

    // readallBytes
    private byte[] readFileToBytes(File file) throws IOException {
        try (FileInputStream fis = new FileInputStream(file);
             ByteArrayOutputStream baos = new ByteArrayOutputStream()) {

            byte[] buffer = new byte[1024];
            int read;
            while ((read = fis.read(buffer)) != -1) {
                baos.write(buffer, 0, read);
            }
            return baos.toByteArray();
        }
    }
}
