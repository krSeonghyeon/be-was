package webserver.config;

import db.ArticleDatabase;
import db.UserDatabase;
import webserver.argument.resolver.ArgumentResolver;
import webserver.argument.resolver.CurrentUserArgumentResolver;
import webserver.core.Dispatcher;
import webserver.dispatch.*;
import webserver.exception.BasicExceptionHandler;
import webserver.exception.HandlerExceptionResolver;
import webserver.filter.AccessLogFilter;
import webserver.filter.RequestFilter;
import webserver.handler.*;
import webserver.interceptor.AuthInterceptor;
import webserver.interceptor.HandlerInterceptor;
import webserver.router.Router;
import webserver.staticresource.StaticResourceHandler;
import webserver.view.TemplateRenderer;
import webserver.view.ViewResolver;

import java.util.List;

public class AppConfig {

    private final StaticResourceHandler staticResourceHandler = new StaticResourceHandler();
    private final TemplateRenderer templateRenderer = new TemplateRenderer();
    private final ViewResolver viewResolver = new ViewResolver(templateRenderer);
    private final UserDatabase userDatabase = new UserDatabase();
    private final ArticleDatabase articleDatabase = new ArticleDatabase();
    private final Router router = createRouter();

    public Dispatcher dispatcher() {
        return new Dispatcher(
                handlerMappings(),
                handlerAdapters(),
                exceptionResolvers(),
                viewResolver
        );
    }

    public List<HandlerMapping> handlerMappings() {
        return List.of(
                new RouterHandlerMapping(router, interceptors()),
                new StaticResourceHandlerMapping(staticResourceHandler)
        );
    }

    public List<HandlerAdapter> handlerAdapters() {
        List<ArgumentResolver> resolvers = List.of(
                new CurrentUserArgumentResolver()
        );

        return List.of(
                new SimpleHandlerAdapter(resolvers),
                new StaticResourceHandlerAdapter()
        );
    }

    public List<HandlerExceptionResolver> exceptionResolvers() {
        return List.of(
                new BasicExceptionHandler()
        );
    }

    public List<RequestFilter> requestFilters() {
        return List.of(
                new AccessLogFilter()
        );
    }

    public List<HandlerInterceptor> interceptors() {
        return List.of(
                new AuthInterceptor()
        );
    }

    private Router createRouter() {
        Router router = new Router();
        router.register("POST", "/create", new CreateUserHandler(userDatabase));
        router.register("POST", "/login", new LoginHandler(userDatabase));
        router.register("POST", "/logout", new LogoutHandler());
        router.register("POST", "/article", new CreateArticleHandler(articleDatabase));
        router.register("GET", "/", new IndexHandler(articleDatabase));
        router.register("GET", "/mypage", new MyPageHandler());
        router.register("GET", "/article", new ArticlePageHandler());
        router.register("GET", "/comment", new CommentHandler());
        return router;
    }
}
