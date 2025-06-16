package com.strollingpasta.bingo;

import java.util.List;
import java.util.Map;

public class OverpassElement {
    private String type;
    private long id;
    private List<Long> nodes;
    private Map<String, String> tags;

    public String getType() {
        return type;
    }

    public long getId() {
        return id;
    }

    public List<Long> getNodes() {
        return nodes;
    }

    public Map<String, String> getTags() {
        return tags;
    }
}
