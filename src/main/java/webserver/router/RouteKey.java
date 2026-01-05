package webserver.router;

public record RouteKey(
        String method,
        String path
) {
}
