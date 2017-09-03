package net.hokiegeek.finra.store;

import org.springframework.data.annotation.Id;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

public class FileRecord {
    @Id private String dbId;

    private String id;
    private Date storedTimestamp;
    private String originalFilename;
    private Long contentLength;
    private String storedPath;
    private Map<String, String> metadata;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public Date getStoredTimestamp() {
        return storedTimestamp;
    }

    public void setStoredTimestamp(Date storedTimestamp) {
        this.storedTimestamp = storedTimestamp;
    }

    public String getOriginalFilename() {
        return originalFilename;
    }

    public void setOriginalFilename(String originalFilename) {
        this.originalFilename = originalFilename;
    }

    public Long getContentLength() {
        return contentLength;
    }

    public void setContentLength(Long contentLength) {
        this.contentLength = contentLength;
    }

    public String getStoredPath() {
        return storedPath;
    }

    public void setStoredPath(String storedPath) {
        this.storedPath = storedPath;
    }

    public Map<String, String> getMetadata() {
        return new HashMap(this.metadata);
    }

    public void setMetadata(Map<String, String> metadata) {
        this.metadata = new HashMap(metadata);
    }

    @Override
    public String toString() {
        return this.originalFilename + ": " + this.id;
    }
}
