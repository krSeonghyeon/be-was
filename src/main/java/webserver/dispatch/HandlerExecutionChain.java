package webserver.dispatch;

import webserver.http.HttpRequest;
import webserver.http.HttpResponse;
import webserver.interceptor.HandlerInterceptor;

import java.util.List;

public class HandlerExecutionChain {

    private final Object handler;
    private final List<HandlerInterceptor> interceptors;
    private int interceptorIndex = -1;

    public HandlerExecutionChain(Object handler, List<HandlerInterceptor> interceptors) {
        this.handler = handler;
        this.interceptors = interceptors;
    }

    public Object getHandler() {
        return handler;
    }

    public boolean applyPreHandle(HttpRequest request, HttpResponse response) {
        for (int i = 0; i < interceptors.size(); i++) {
            if (!interceptors.get(i).preHandle(request, response, handler)) {
                interceptorIndex = i - 1;
                return false;
            }
            interceptorIndex = i;
        }
        return true;
    }

    public void applyPostHandle(HttpRequest request, HttpResponse response) {
        for (int i = interceptors.size() - 1; i >= 0; --i) {
            interceptors.get(i).postHandle(request, response);
        }
    }

    public void triggerAfterCompletion(HttpRequest request, Exception ex) {
        for (int i = interceptorIndex; i >= 0; --i) {
            interceptors.get(i).afterCompletion(request, ex);
        }
    }
}
