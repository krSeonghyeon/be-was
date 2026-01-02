package webserver.core;

import java.io.*;
import java.net.Socket;
import java.util.Map;

import db.Database;
import model.user.User;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import webserver.http.HttpRequest;
import webserver.http.HttpRequestParser;
import webserver.http.HttpResponse;
import webserver.staticresource.StaticResourceHandler;

public class RequestHandler implements Runnable {
    private static final Logger logger = LoggerFactory.getLogger(RequestHandler.class);

    private final Socket connection;
    private final StaticResourceHandler staticResourceHandler;

    public RequestHandler(Socket connectionSocket, StaticResourceHandler staticResourceHandler) {
        this.connection = connectionSocket;
        this.staticResourceHandler = staticResourceHandler;
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

            HttpResponse response;
            if (request.path().equals("/create")) {
                response = handleCreateUser(request);
            } else {
                response = staticResourceHandler.handle(request.path());
            }
            response.writeTo(dos);
        } catch (IOException e) {
            logger.error(e.getMessage());
        }
    }

    private HttpResponse handleCreateUser(HttpRequest request) {
        if (request.parameters().isEmpty()) {
            return HttpResponse.badRequest();
        }

        User user = createUser(request.parameters());
        Database.addUser(user);
        return HttpResponse.redirect("/login");
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
}
