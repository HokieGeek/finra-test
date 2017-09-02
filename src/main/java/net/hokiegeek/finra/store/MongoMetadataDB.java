package net.hokiegeek.finra.store;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Example;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

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
    public List<FileRecord> getByMetadata(Map<String, String> metadata) {
        FileRecord rec = new FileRecord();
        rec.setMetadata(metadata);
        return this.db.findAll(Example.of(rec));
    }
}
