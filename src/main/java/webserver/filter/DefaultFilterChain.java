package webserver.filter;

import webserver.http.HttpRequest;

import java.util.List;

public class DefaultFilterChain implements FilterChain {

    private final List<RequestFilter> filters;
    private final Runnable dispatcher;
    private int index = 0;

    public DefaultFilterChain(List<RequestFilter> filters, Runnable dispatcher) {
        this.filters = filters;
        this.dispatcher = dispatcher;
    }

    @Override
    public void doFilter(HttpRequest request) throws Exception {
        if (index < filters.size()) {
            RequestFilter nextFilter = filters.get(index++);
            nextFilter.doFilter(request, this);
        } else {
            dispatcher.run();
        }
    }
}
