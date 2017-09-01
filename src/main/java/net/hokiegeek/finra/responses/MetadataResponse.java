package net.hokiegeek.finra.responses;

import net.hokiegeek.finra.store.FileMetadata;

import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.HashMap;
import java.util.Map;

public class MetadataResponse {
    private String filename;
    private Map<String, String> metadata;

    public MetadataResponse(FileMetadata metadata) {
        this.loadMetadata(metadata);
    }

    private void loadMetadata(FileMetadata metadata) {
        Path path = Paths.get(metadata.getPath());
        this.filename = path.getName(path.getNameCount()-1).toString();
        this.metadata = metadata.getMetadata();
    }

    public String getFilename() {
        return filename;
    }

    public Map<String, String> getMetadata() {
        return new HashMap(this.metadata);
    }
}
