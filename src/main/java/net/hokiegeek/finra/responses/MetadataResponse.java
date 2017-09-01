package net.hokiegeek.finra.responses;

import net.hokiegeek.finra.store.FileMetadata;

import java.nio.file.Path;
import java.nio.file.Paths;

public class MetadataResponse {
    private String filename;

    public MetadataResponse(FileMetadata metadata) {
        this.loadMetadata(metadata);
    }

    private void loadMetadata(FileMetadata metadata) {
        Path path = Paths.get(metadata.getPath());
        this.filename = path.getName(path.getNameCount()-1).toString();
    }

    public String getFilename() {
        return filename;
    }
}

