package webserver.view;

import java.io.InputStream;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class TemplateRenderer {

    private static final Pattern IF_PATTERN =
            Pattern.compile("\\{\\{#if ([^}]+)}}([\\s\\S]*?)\\{\\{/if}}"); // {{#if key}} ... {{/if}}

    private static final Pattern VAR_PATTERN =
            Pattern.compile("\\{\\{(\\w+)}}");

    public String render(String templatePath, Map<String, Object> model) {
        if (model == null) {
            model = Map.of();
        }

        String html = load(templatePath);
        if (html == null) return null; // null 반환되면 path를 못찾은 것, 404 처리 필요

        html = renderIf(html, model);
        html = renderVariables(html, model);

        return html;
    }

    private String renderIf(String html, Map<String, Object> model) {
        Matcher matcher = IF_PATTERN.matcher(html);
        StringBuilder result = new StringBuilder();
        int lastIndex = 0;

        while (matcher.find()) {
            result.append(html, lastIndex, matcher.start());

            String expr = matcher.group(1).trim();
            String body = matcher.group(2);

            if (evaluate(expr, model)) {
                result.append(body);
            }

            lastIndex = matcher.end();
        }

        result.append(html.substring(lastIndex));
        return result.toString();
    }

    private boolean evaluate(String expr, Map<String, Object> model) {
        if (expr.startsWith("!")) {
            String key = expr.substring(1).trim();
            return !Boolean.TRUE.equals(model.get(key));
        }
        return Boolean.TRUE.equals(model.get(expr));
    }

    private String renderVariables(String html, Map<String, Object> model) {
        Matcher matcher = VAR_PATTERN.matcher(html);
        StringBuilder result = new StringBuilder();

        while (matcher.find()) {
            String key = matcher.group(1);
            Object value = model.get(key);

            matcher.appendReplacement(
                    result,
                    value == null ? "" : Matcher.quoteReplacement(value.toString())
            );
        }

        matcher.appendTail(result);
        return result.toString();
    }

    private String load(String templatePath) {
        String resolvedPath = templatePath;

        if (!templatePath.endsWith(".html")) {
            resolvedPath = templatePath + "/index.html";
        }

        try (InputStream is = getClass().getClassLoader().getResourceAsStream("templates/" + resolvedPath)) {
            if (is == null) return null;
            return new String(is.readAllBytes());
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}
