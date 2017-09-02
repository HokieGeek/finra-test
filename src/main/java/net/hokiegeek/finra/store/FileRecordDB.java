package net.hokiegeek.finra.store;

import java.util.ArrayList;
import java.util.List;

public interface FileRecordDB {
    public void store(FileRecord record);
    public FileRecord getById(String id);
    public List<FileRecord> getByMetadata(String field, String value);
}
