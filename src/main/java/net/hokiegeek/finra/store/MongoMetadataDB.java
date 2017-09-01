package net.hokiegeek.finra.store;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component("mongodb")
public class MongoMetadataDB implements FileMetadataDB {
    private MongoDB db;

    @Autowired
    public MongoMetadataDB(MongoDB db) {
        this.db = db;
        this.db.deleteAll();
    }

    @Override
    public void store(FileMetadata metadata) {
        this.db.save(metadata);
    }

    @Override
    public FileMetadata getById(String id) {
        return this.db.findById(id);
    }
}
