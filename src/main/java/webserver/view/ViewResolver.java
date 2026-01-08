package webserver.view;

public class ViewResolver {

    private static final String PREFIX = "redirect:";
    private final TemplateRenderer renderer;

    public ViewResolver(TemplateRenderer renderer) {
        this.renderer = renderer;
    }

    public View resolveViewName(String viewName) {
        if (viewName.startsWith(PREFIX)) {
            String location = viewName.substring(PREFIX.length());
            return new RedirectView(location);
        }

        return new HtmlView(viewName, renderer);
    }
}
