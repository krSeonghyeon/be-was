package webserver.dispatch;

import webserver.argument.resolver.ArgumentResolver;
import webserver.http.HttpRequest;
import webserver.http.HttpResponse;
import webserver.handler.Handler;
import webserver.view.ModelAndView;

import java.lang.reflect.Method;
import java.lang.reflect.Parameter;
import java.util.ArrayList;
import java.util.List;

public class SimpleHandlerAdapter implements HandlerAdapter {

    private final List<ArgumentResolver> argumentResolvers;

    public SimpleHandlerAdapter(List<ArgumentResolver> argumentResolvers) {
        this.argumentResolvers = argumentResolvers;
    }

    @Override
    public boolean supports(Object handler) {
        return handler instanceof Handler;
    }

    @Override
    public ModelAndView handle(HttpRequest request, HttpResponse response, Object handler) {
        try {
            Method handleMethod = findHandleMethod(handler);
            Object[] args = resolveArguments(handleMethod, request, response);
            return (ModelAndView) handleMethod.invoke(handler, args);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    private Method findHandleMethod(Object handler) {
        for (Method method : handler.getClass().getMethods()) {
            if (method.getName().equals("handle")) {
                return method;
            }
        }
        throw new IllegalStateException("No handle() method found in handler " + handler.getClass());
    }

    private Object[] resolveArguments(Method method, HttpRequest request, HttpResponse response) {
        Parameter[] params = method.getParameters();
        List<Object> args = new ArrayList<>();

        for (Parameter p : params) {
            if (p.getType().equals(HttpRequest.class)) {
                args.add(request);
                continue;
            }

            if (p.getType().equals(HttpResponse.class)) {
                args.add(response);
                continue;
            }

            boolean resolved = false;
            for (ArgumentResolver resolver : argumentResolvers) {
                if (resolver.supports(p)) {
                    args.add(resolver.resolve(request, response, p));
                    resolved = true;
                    break;
                }
            }

            if (!resolved) {
                args.add(null);
            }
        }

        return args.toArray();
    }
}
