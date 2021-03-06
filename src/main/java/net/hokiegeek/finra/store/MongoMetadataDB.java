package net.hokiegeek.finra.store;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Example;
import org.springframework.stereotype.Component;

import java.lang.IllegalArgumentException;
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
    public void delete(String id) throws IllegalArgumentException {
        this.db.delete(id);
    }

    @Override
    public Long count() {
        return this.db.count();
    }

    @Override
    public FileRecord getById(String id) {
        return this.db.findById(id);
    }

    @Override
    public List<FileRecord> getAll() {
        return this.db.findAll();
    }

    @Override
    public List<FileRecord> getByMetadata(Map<String, String> metadata) {
        FileRecord rec = new FileRecord();
        rec.setMetadata(metadata);
        return this.db.findAll(Example.of(rec));
    }
}
