package webserver.interceptor;

import webserver.http.HttpRequest;
import webserver.http.HttpResponse;

public interface HandlerInterceptor {

    boolean preHandle(HttpRequest request);

    void postHandle(HttpRequest request, HttpResponse response);

    void afterCompletion(HttpRequest request, Exception ex);
}
