package webserver;

import java.io.*;
import java.net.Socket;
import java.util.Map;

import db.Database;
import model.User;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import webserver.util.QueryStringParser;

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
                if (params.isEmpty()) {
                    response400(dos);
                    return;
                }

                User user = createUser(params);
                Database.addUser(user);

                response302(dos, "/login");
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
            response404(dos);
            return;
        }

        byte[] body = readFileToBytes(file);
        ContentType contentType = ContentType.fromFileName(file.getName());
        response200Header(dos, body.length, contentType.getMimeType());
        responseBody(dos, body);
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

    private void response200Header(DataOutputStream dos, int lengthOfBodyContent, String contentType) {
        try {
            dos.writeBytes("HTTP/1.1 200 OK \r\n");
            dos.writeBytes("Content-Type: " + contentType + "\r\n");
            dos.writeBytes("Content-Length: " + lengthOfBodyContent + "\r\n");
            dos.writeBytes("\r\n");
        } catch (IOException e) {
            logger.error(e.getMessage());
        }
    }

    private void response302(DataOutputStream dos, String location) {
        try {
            dos.writeBytes("HTTP/1.1 302 Found \r\n");
            dos.writeBytes("Location: " + location + "\r\n");
            dos.writeBytes("\r\n");
            dos.flush();
        } catch (IOException e) {
            logger.error(e.getMessage());
        }
    }

    private void response400(DataOutputStream dos) {
        try {
            dos.writeBytes("HTTP/1.1 400 Bad Request \r\n");
            dos.writeBytes("Content-Length: 0\r\n");
            dos.writeBytes("\r\n");
            dos.flush();
        } catch (IOException e) {
            logger.error(e.getMessage());
        }
    }

    private void response404(DataOutputStream dos) {
        try {
            dos.writeBytes("HTTP/1.1 404 Not Found\r\n");
            dos.writeBytes("Content-Length: 0\r\n");
            dos.writeBytes("\r\n");
            dos.flush();
        } catch (IOException e) {
            logger.error(e.getMessage());
        }
    }

    private void responseBody(DataOutputStream dos, byte[] body) {
        try {
            dos.write(body, 0, body.length);
            dos.flush();
        } catch (IOException e) {
            logger.error(e.getMessage());
        }
    }
}
