package webserver.interceptor;

import webserver.http.HttpRequest;
import webserver.http.HttpResponse;

public interface HandlerInterceptor {

    boolean preHandle(HttpRequest request, HttpResponse response, Object handler);

    void postHandle(HttpRequest request, HttpResponse response);

    void afterCompletion(HttpRequest request, Exception ex);
}
