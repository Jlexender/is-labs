package org.example.dto;

public record DummyDto(Long id, String name) {
    /**
     * Returns the JSON field schema for front-end.
     * @return JSON field schema map with field names as keys and their types as values.
     */
    public static java.util.Map<String, String> getJsonFieldSchema() {
        return java.util.Map.of(
            "id", "number",
            "name", "string"
        );
    }
}