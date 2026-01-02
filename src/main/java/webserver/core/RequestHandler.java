package webserver.core;

import java.io.*;
import java.net.Socket;
import java.util.Map;

import db.Database;
import model.User;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import webserver.http.ContentType;
import webserver.http.HttpRequest;
import webserver.http.HttpRequestParser;
import webserver.http.HttpResponse;

public class RequestHandler implements Runnable {
    private static final Logger logger = LoggerFactory.getLogger(RequestHandler.class);

    private Socket connection;

    public RequestHandler(Socket connectionSocket) {
        this.connection = connectionSocket;
    }

    public void run() {
        logger.debug("New Client Connect! Connected IP : {}, Port : {}", connection.getInetAddress(),
                connection.getPort());

        try (InputStream in = connection.getInputStream();
             OutputStream out = connection.getOutputStream();
             BufferedReader br = new BufferedReader(new InputStreamReader(in));
             DataOutputStream dos = new DataOutputStream(out);
        ) {
            HttpRequest request = HttpRequestParser.parser(br);
            if (request == null) {
                return;
            }

            if (request.path().equals("/create")) {
                HttpResponse response;
                if (request.parameters().isEmpty()) {
                    response = HttpResponse.badRequest();
                } else {
                    User user = createUser(request.parameters());
                    Database.addUser(user);
                    response = HttpResponse.redirect("/login");
                }
                response.writeTo(dos);
                return;
            }

            handleStaticResource(request.path(), dos);
        } catch (IOException e) {
            logger.error(e.getMessage());
        }
    }

    private User createUser(Map<String, String[]> params) {
        String userId = getFirst(params, "userId");
        String password = getFirst(params, "password");
        String name = getFirst(params, "name");
        return new User(userId, password, name);
    }

    private String getFirst(Map<String, String[]> params, String key) {
        String[] values = params.get(key);
        return (values == null || values.length == 0) ? null : values[0];
    }

    private void handleStaticResource(String path, DataOutputStream dos) throws IOException {
        File file = new File("src/main/resources/static" + path);

        if (file.isDirectory()) {
            file = new File(file, "index.html");
        }

        if (!file.exists()) {
            HttpResponse.notFound().writeTo(dos);
            return;
        }

        byte[] body = readFileToBytes(file);
        ContentType contentType = ContentType.fromFileName(file.getName());
        HttpResponse response = HttpResponse.ok(body, contentType.getMimeType());
        response.writeTo(dos);
    }

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
