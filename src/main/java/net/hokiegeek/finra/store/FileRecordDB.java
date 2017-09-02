package net.hokiegeek.finra.store;

public interface FileRecordDB {
    public void store(FileRecord record);
    public FileRecord getById(String id);
}
