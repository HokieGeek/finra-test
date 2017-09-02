package net.hokiegeek.finra.store;

import org.springframework.data.annotation.Id;

import java.util.HashMap;
import java.util.Map;

public class FileRecord {
    @Id private String dbId;

    private String id;
    private String path;
    private Map<String, String> metadata;

    public FileRecord(Map<String, String> metadata) {
        this.metadata = metadata;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }

    public Map<String, String> getMetadata() {
        return new HashMap(this.metadata);
    }

    @Override
    public String toString() {
        return id;
    }
}
