package net.hokiegeek.finra.store;

import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;
import org.springframework.boot.test.mock.mockito.MockBean;

import net.hokiegeek.finra.store.FileRecordDB;

public class FileStoreTest {
    @MockBean
    private FileRecordDB mockDb;

    @Before
    public void setup() {
    }

    @Test
    @Ignore("TODO")
    public void testStoreFile() {
        // String storeFile(MultipartFile file, Map<String, String> metadata) {
    }


    @Test
    @Ignore("TODO")
    public void testGetFileRecord() {
        // FileRecord getFileRecord(String id)
        // return db.getById(id);
    }

    @Test
    @Ignore("TODO")
    public void testGetFileAsResource() {
        // Resource getFileAsResource(String id)
    }
}
