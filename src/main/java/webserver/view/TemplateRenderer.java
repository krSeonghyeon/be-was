package webserver.view;

import java.io.InputStream;

public class TemplateRenderer {

    public String render(String templatePath) {
        try (InputStream is = getClass().getClassLoader().getResourceAsStream("templates/" + templatePath)) {
            // TODO:패키지내부 패키지 처리 필요 (/templates/mypage/index.html) 등

            if (is == null) {
                return null; // 404 처리 필요
            }

            return new String(is.readAllBytes());
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}
