package webserver.http;

public record UploadFile(
        String filename,
        String contentType,
        byte[] content
) {


}
