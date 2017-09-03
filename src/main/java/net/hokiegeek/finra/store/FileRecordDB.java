package net.hokiegeek.finra.store;

import java.lang.IllegalArgumentException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public interface FileRecordDB {
    public void store(FileRecord record);
    public void delete(String id) throws IllegalArgumentException;
    public Long count();
    public FileRecord getById(String id);
    public List<FileRecord> getAll();
    public List<FileRecord> getByMetadata(Map<String, String> metadata);
}
