package webserver.util;

import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class QueryStringParser {

    private QueryStringParser() {}

    public static Map<String, String[]> parse(String query) {
        Map<String, List<String>> temp = new HashMap<>();

        if (query == null || query.isEmpty()) {
            return new HashMap<>();
        }

        String[] pairs = query.split("&");
        for (String pair : pairs) {
            if (pair.isEmpty()) continue;
            String[] kv = pair.split("=", 2);
            String key = kv[0];
            String value = kv.length > 1 ? decodeUtf8(kv[1]) : "";
            List<String> values = temp.computeIfAbsent(key, k -> new ArrayList<>());
            values.add(value);
        }

        Map<String, String[]> parameterMap = new HashMap<>();
        for (Map.Entry<String, List<String>> entry : temp.entrySet()) {
            parameterMap.put(
                    entry.getKey(),
                    entry.getValue().toArray(new String[0])
            );
        }

        return parameterMap;
    }

    private static String decodeUtf8(String value) {
        try {
            return URLDecoder.decode(value, "UTF-8");
        } catch (UnsupportedEncodingException e) {
            throw new RuntimeException(e);
        }
    }
}
