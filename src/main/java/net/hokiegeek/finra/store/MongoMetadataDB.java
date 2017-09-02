package net.hokiegeek.finra.store;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

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

    @Override
    public List<FileRecord> getByMetadata(String field, String value) {
        // return this.db.findAll(id);
        List<FileRecord> ids = new ArrayList<>();
        // TODO
        return ids;
    }
}
