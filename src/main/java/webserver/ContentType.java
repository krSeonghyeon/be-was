package webserver;

public enum ContentType {
    HTML("html", "text/html"),
    CSS("css", "text/css"),
    JS("js", "application/javascript"),
    PNG("png", "image/png"),
    JPG("jpg", "image/jpeg"),
    ICO("ico", "image/x-icon"),
    OCTET_STREAM("", "application/octet-stream");

    private final String extension;
    private final String mimeType;

    ContentType(String extension, String mimeType) {
        this.extension = extension;
        this.mimeType = mimeType;
    }

    public String getMimeType() {
        return mimeType;
    }

    public static ContentType fromFileName(String fileName) {
        int dotIndex = fileName.lastIndexOf('.');
        if (dotIndex == -1) {
            return OCTET_STREAM;
        }

        String ext = fileName.substring(dotIndex + 1).toLowerCase();

        for (ContentType type : values()) {
            if (type.extension.equals(ext)) {
                return type;
            }
        }

        return OCTET_STREAM;
    }
}
