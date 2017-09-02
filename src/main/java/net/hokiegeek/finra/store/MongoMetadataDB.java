package net.hokiegeek.finra.store;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component("mongodb")
public class MongoMetadataDB implements FileRecordDB {
    private MongoDB db;

    @Autowired
    public MongoMetadataDB(MongoDB db) {
        this.db = db;
        this.db.deleteAll();
    }

    @Override
    public void store(FileRecord record) {
        this.db.save(record);
    }

    @Override
    public FileRecord getById(String id) {
        return this.db.findById(id);
    }
}
