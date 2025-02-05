package com.example.catalog.model;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

public class Track {
    private String id;
    private String name;
    private boolean explicit;
    private String uri;

    @JsonProperty("duration_ms")
    private int durationMs;

    public Track(String id, String name, boolean explicit, String uri, int durationMs) {
        this.id = id;
        this.name = name;
        this.explicit = explicit;
        this.uri = uri;
        this.durationMs = durationMs;
    }

    @JsonCreator
    public Track(@JsonProperty("id") String id,
                 @JsonProperty("name") String name) {
        this.id = id;
        this.name = name;
        this.explicit = false;
        this.uri = "";
        this.durationMs = 0;
    }


    // Getters and Setters
    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public boolean isExplicit() {
        return explicit;
    }

    public void setExplicit(boolean explicit) {
        this.explicit = explicit;
    }

    public String getUri() {
        return uri;
    }

    public void setUri(String uri) {
        this.uri = uri;
    }

    public int getDurationMs() {
        return durationMs;
    }

    public void setDurationMs(int durationMs) {
        this.durationMs = durationMs;
    }
}