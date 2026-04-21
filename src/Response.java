import java.util.Map;

public record Response(
        int statusCode,
        String statusMessage,
        Map<String, String> headers,
        String body
) {}