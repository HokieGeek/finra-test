package net.hokiegeek.finra.store;

import java.io.IOException;
import java.io.InputStream;
import java.security.NoSuchAlgorithmException;
import java.util.Hashtable;
import java.util.Map;

import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.core.io.Resource;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.test.context.junit4.SpringRunner;

import net.hokiegeek.finra.store.FileRecord;
import net.hokiegeek.finra.store.FileRecordDB;
import net.hokiegeek.finra.store.Utils;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.BDDMockito.*;

@RunWith(SpringRunner.class)
@SpringBootTest
public class FileStoreTest {
    @Autowired
    private FileStore store;

    @MockBean
    private FileRecordDB mockFileRecordDB;

    @MockBean
    private Utils mockUtils;

    private static String dummyId;
    private static Map<String, String> dummyMetadata;
    private static FileRecord dummyFileRecord;
    private static MockMultipartFile mockMultipartFile;

    @Before
    public void setup() {
        dummyId = "plok";

        dummyMetadata = new Hashtable<>();
        dummyMetadata.put("rab", "oof");

        Long dummyContentLength = 42L;

        dummyFileRecord = new FileRecord();
        dummyFileRecord.setId(dummyId);
        dummyFileRecord.setMetadata(dummyMetadata);
        dummyFileRecord.setStoredPath("/blah");
        dummyFileRecord.setContentLength(dummyContentLength);

        // Create the mock file and get the ID which would be generated for it
        mockMultipartFile = new MockMultipartFile("file", "asdfsadfasdfasdf".getBytes());
        try {
            dummyId = this.mockUtils.getSha1FromInputStream(mockMultipartFile.getInputStream());

            // Setup the behavior of the file record db so that it returns the dummy record
            when(mockFileRecordDB.getById(dummyId)).thenReturn(dummyFileRecord);
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Test
    public void testStoreFile() {
        // Start with a couple "conditions normal" tests
        try {
            // Test with filled values for all arguments
            String id_filledValues = store.storeFile(mockMultipartFile, dummyMetadata);
            assertThat(id_filledValues).isEqualTo(dummyId);

            // Test with no metadata but a valid file
            String id_noMetadata = store.storeFile(mockMultipartFile, null);
            assertThat(id_noMetadata).isEqualTo(dummyId);
        } catch (IOException e) {
            e.printStackTrace();
        }

        // Throw in a destructive test
        Throwable thrown = catchThrowable(() -> store.storeFile(null, dummyMetadata));
        assertThat(thrown).isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    @Ignore("Added this out of completeness, but not fleshing out for this exam")
    public void testDeleteFileById() {
        // public boolean deleteFileById(String id) {
    }

    @Test
    @Ignore("Added this out of completeness, but not fleshing out for this exam")
    public void testCount() {
        // public Long count() {
    }

    @Test
    @Ignore("Added this out of completeness, but not fleshing out for this exam")
    public void testGetAllRecords() {
        // public synchronized List<FileRecord> getAllRecords() {
    }

    @Test
    public void testGetFileRecord() {
        FileRecord record = store.getFileRecord(dummyId);
        assertThat(record).isEqualTo(dummyFileRecord);

        // Destructive test
        Throwable thrown = catchThrowable(() -> store.getFileRecord(null));
        assertThat(thrown).isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    public void testGetFileAsResource() {
        Resource file = store.getFileAsResource(dummyId);
        try {
            assertThat(file.getFile().getAbsolutePath()).isEqualTo(dummyFileRecord.getStoredPath());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
