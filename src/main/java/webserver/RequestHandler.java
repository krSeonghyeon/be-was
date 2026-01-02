package webserver;

import java.io.*;
import java.net.Socket;
import java.util.Map;

import db.Database;
import model.User;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import webserver.util.QueryStringParser;
import webserver.http.HttpResponse;

import javax.xml.crypto.Data;

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
            String requestLine = br.readLine();
            if (requestLine == null || requestLine.isEmpty()) {
                return;
            }

            String[] tokens = requestLine.split(" ");
            String url = tokens[1];

            String path = url;
            String query = "";

            if (url.contains("?")) {
                String[] split = url.split("\\?");
                path = split[0];
                query = split[1];
            }

            Map<String, String[]> params = QueryStringParser.parse(query);

            printRequestLog(requestLine, br);

            if (path.equals("/create")) {
                HttpResponse response;

                if (params.isEmpty()) {
                    response = HttpResponse.badRequest();
                } else {
                    User user = createUser(params);
                    Database.addUser(user);
                    response = HttpResponse.redirect("/login");
                }

                response.writeTo(dos);
                return;
            }

            handleStaticResource(path, dos);
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

    private void printRequestLog(String requestLine, BufferedReader br) throws IOException {
        StringBuilder requestLog = new StringBuilder();
        requestLog.append(requestLine).append("\n");
        while ((requestLine = br.readLine()) != null && !requestLine.isEmpty()) {
            requestLog.append(requestLine).append("\n");
        }
        logger.debug(requestLog.toString());
    }
}
