package webserver.filter;

import webserver.http.HttpRequest;

public interface FilterChain {

    void doFilter(HttpRequest request) throws Exception;
}
