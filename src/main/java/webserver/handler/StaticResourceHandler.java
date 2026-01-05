package webserver.handler;

import webserver.http.ContentType;
import webserver.http.HttpResponse;

import java.io.*;

public class StaticResourceHandler {

    private static final String BASE_PATH = "src/main/resources/static";

    public boolean exists(String path) {
        return resolve(path).exists();
    }

    public HttpResponse handle(String path) throws IOException {
        File file = resolve(path);

        if (!file.exists()) {
            return HttpResponse.notFound();
        }

        byte[] body;
        try (InputStream is = new FileInputStream(file)) {
            body = is.readAllBytes();
        }

        ContentType contentType = ContentType.fromFileName(file.getName());
        return HttpResponse.ok(body, contentType.getMimeType());
    }

    private File resolve(String path) {
        if (path.equals("/")) {
            path = "/index.html";
        }

        File file = new File(BASE_PATH + path);

        if (file.isDirectory()) {
            file = new File(file, "index.html");
        }

        return file;
    }
}
