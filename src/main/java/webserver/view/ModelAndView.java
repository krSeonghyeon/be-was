package webserver.view;

import java.util.HashMap;
import java.util.Map;

public class ModelAndView {

    private final String viewName;
    private Map<String, Object> model = new HashMap<>();

    public ModelAndView(String viewName, Map<String, Object> model) {
        this.viewName = viewName;
        this.model = model;
    }

    public ModelAndView(String viewName) {
        this.viewName = viewName;
    }

    public String getViewName() {
        return viewName;
    }

    public Map<String, Object> getModel() {
        return model;
    }
}
