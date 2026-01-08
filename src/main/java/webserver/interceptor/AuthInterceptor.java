package webserver.interceptor;

import model.user.User;
import webserver.http.HttpRequest;
import webserver.http.HttpResponse;
import webserver.http.HttpStatus;
import db.SessionManager;

public class AuthInterceptor implements HandlerInterceptor {

    @Override
    public boolean preHandle(HttpRequest request, HttpResponse response, Object handler) {

        User user = getUser(request);

        if (user != null) {
            request.setAttribute("USER", user); // TODO: ArguementResolver로 분리
        }

        if (isProtectedPath(request.getPath())) {
            if (user == null) {
                response.setStatus(HttpStatus.FOUND);
                response.setHeader("Location", "/login");
                return false;
            }
        }

        return true;
    }

    private User getUser(HttpRequest request) {
        String sid = request.getCookie("SID");
        if (sid == null) return null;
        return SessionManager.getUser(sid);
    }

    private boolean isProtectedPath(String path) {
        return path.startsWith("/mypage");
    }

    @Override
    public void postHandle(HttpRequest request, HttpResponse response) {

    }

    @Override
    public void afterCompletion(HttpRequest request, Exception ex) {

    }
}
