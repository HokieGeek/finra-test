package net.hokiegeek.finra.responses;

import java.util.HashMap;
import java.util.Map;

public class MetadataResponse {
    private Map<String, String> metadata;

    public void setMetadata(Map<String, String> metadata) {
        this.metadata = new HashMap(metadata);
    }

    public Map<String, String> getMetadata() {
        return new HashMap(this.metadata);
    }
}
