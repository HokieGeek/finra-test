package net.hokiegeek.finra.store;

import org.springframework.stereotype.Component;
import org.springframework.data.mongodb.repository.MongoRepository;

@Component("mongodb")
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
