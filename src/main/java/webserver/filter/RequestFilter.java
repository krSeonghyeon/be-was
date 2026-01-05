package webserver.filter;

import webserver.http.HttpRequest;

public interface RequestFilter {

    void doFilter(HttpRequest request);
}
