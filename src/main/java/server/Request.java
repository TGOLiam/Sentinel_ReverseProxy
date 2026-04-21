package server;

import java.util.Map;

public record Request(
        String method,
        String path,
        String version,
        Map<String, String> headers,
        String body
) {}