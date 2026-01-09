package webserver.argument;

import model.user.User;
import webserver.argument.annotation.CurrentUser;
import webserver.http.HttpRequest;
import webserver.http.HttpResponse;

import java.lang.reflect.Parameter;

public class CurrentUserArgumentResolver implements ArgumentResolver {

    @Override
    public boolean supports(Parameter parameter) {
        boolean annotated = parameter.isAnnotationPresent(CurrentUser.class);
        boolean typeMatch = parameter.getType().equals(User.class);
        return annotated && typeMatch;
    }

    @Override
    public Object resolve(HttpRequest request, HttpResponse response, Parameter parameter) {
        return request.getAttribute("USER");
    }
}
