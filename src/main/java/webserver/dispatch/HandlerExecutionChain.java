package webserver.dispatch;

import webserver.http.HttpRequest;
import webserver.http.HttpResponse;
import webserver.interceptor.HandlerInterceptor;

import java.util.List;

public class HandlerExecutionChain {

    private final Object handler;
    private final List<HandlerInterceptor> interceptors;

    public HandlerExecutionChain(Object handler, List<HandlerInterceptor> interceptors) {
        this.handler = handler;
        this.interceptors = interceptors;
    }

    public Object getHandler() {
        return handler;
    }

    public boolean applyPreHandle(HttpRequest request) {
        for (HandlerInterceptor interceptor : interceptors) {
            if (!interceptor.preHandle(request)) {
                return false;
            }
        }
        return true;
    }

    public void applyPostHandle(HttpRequest request, HttpResponse response) {
        for (int i = interceptors.size() - 1; i >= 0; --i) {
            interceptors.get(i).postHandle(request, response);
        }
    }

    public void triggerAfterCompletion(HttpRequest request, Exception ex) {
        for (int i = interceptors.size() - 1; i >= 0; --i) {
            interceptors.get(i).afterCompletion(request, ex);
        }
    }
}
