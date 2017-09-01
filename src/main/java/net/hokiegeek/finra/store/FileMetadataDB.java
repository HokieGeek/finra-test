package net.hokiegeek.finra.store;

public interface FileMetadataDB {
    public void store(FileMetadata metadata);
    public FileMetadata getById(String id);
}
