package net.hokiegeek.finra.store;

import org.springframework.data.mongodb.repository.MongoRepository;

// public class MongoMetadataDB implements FileMetadataDB, MongoRepository<FileMetadata, String> {
public class MongoMetadataDB implements FileMetadataDB {
    private FileMetadata dummyMetadata = null;

    @Override
    public void store(FileMetadata metadata) {
        // TODO
        dummyMetadata = metadata;
    }

    @Override
    public FileMetadata getById(String id) {
        // TODO
        return dummyMetadata;
    }
}
