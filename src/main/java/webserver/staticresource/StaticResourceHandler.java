package webserver.staticresource;

import webserver.http.ContentType;
import webserver.http.HttpResponse;
import webserver.http.HttpStatus;

import java.io.*;

public class StaticResourceHandler {

    private static final String BASE_PATH = "src/main/resources/static";

    public boolean exists(String path) {
        return resolve(path).exists();
    }

    public void handle(String path, HttpResponse response) throws IOException {
        File file = resolve(path);

        if (!file.exists()) {
            response.setStatus(HttpStatus.NOT_FOUND);
            return;
        }

        byte[] body;
        try (InputStream is = new FileInputStream(file)) {
            body = is.readAllBytes();
        }

        ContentType contentType = ContentType.fromFileName(file.getName());

        response.setStatus(HttpStatus.OK);
        response.setBody(body);
        response.setHeader("Content-Type", contentType.getMimeType()); // MessageConverter?
        response.setHeader("Content-Length", String.valueOf(body.length));
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
