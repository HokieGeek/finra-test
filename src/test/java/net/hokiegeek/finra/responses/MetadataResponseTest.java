package net.hokiegeek.finra.responses;

import org.junit.BeforeClass;
import org.junit.Test;

import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Hashtable;
import java.util.Map;

import net.hokiegeek.finra.store.FileMetadata;

import static org.junit.Assert.*;

public class MetadataResponseTest {
    private static Map<String, String> dummyMetadata;
    private static FileMetadata dummyFileMetadata;

    @BeforeClass
    public static void setupData() {
        dummyMetadata = new Hashtable<>();
        dummyMetadata.put("key", "val");

        dummyFileMetadata = new FileMetadata(dummyMetadata);
    }

    @Test
    public void testGetMetadata() {
        MetadataResponse response = new MetadataResponse(dummyFileMetadata);

        assertArrayEquals(dummyMetadata.keySet().toArray(), response.getMetadata().keySet().toArray());
        assertArrayEquals(dummyMetadata.values().toArray(), response.getMetadata().values().toArray());
    }
}
