package webserver.interceptor;

import webserver.http.HttpRequest;
import webserver.http.HttpResponse;

public class AuthInterceptor implements HandlerInterceptor{

    @Override
    public boolean preHandle(HttpRequest request) {
        return true; // TODO: 추후 인증로직추가
    }

    @Override
    public void postHandle(HttpRequest request, HttpResponse response) {

    }

    @Override
    public void afterCompletion(HttpRequest request, Exception ex) {

    }
}
