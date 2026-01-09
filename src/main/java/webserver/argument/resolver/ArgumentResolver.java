package webserver.argument.resolver;

import webserver.http.HttpRequest;
import webserver.http.HttpResponse;

import java.lang.reflect.Parameter;

public interface ArgumentResolver {

    boolean supports(Parameter parameter);

    Object resolve(HttpRequest request, HttpResponse response, Parameter parameter);
}
