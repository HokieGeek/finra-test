package net.hokiegeek.finra.responses;

import net.hokiegeek.finra.store.FileMetadata;

import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.HashMap;
import java.util.Map;

public class MetadataResponse {
    private Map<String, String> metadata;

    public MetadataResponse(FileMetadata metadata) {
        this.loadMetadata(metadata);
    }

    private void loadMetadata(FileMetadata metadata) {
        this.metadata = metadata.getMetadata();
    }

    public Map<String, String> getMetadata() {
        return new HashMap(this.metadata);
    }
}
