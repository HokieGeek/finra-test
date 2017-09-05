package net.hokiegeek.finra;

import com.fasterxml.jackson.databind.ObjectMapper;

import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Ignore;
import org.junit.Test;
import org.junit.runner.RunWith;

import org.springframework.beans.factory.annotation.*;
import org.springframework.boot.test.autoconfigure.web.servlet.*;
import org.springframework.boot.test.mock.mockito.*;
import org.springframework.core.io.PathResource;
import org.springframework.core.io.Resource;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.util.MultiValueMap;
import org.springframework.util.LinkedMultiValueMap;

import java.util.Arrays;
import java.util.Hashtable;
import java.util.List;
import java.util.Map;

import net.hokiegeek.finra.store.FileRecord;
import net.hokiegeek.finra.store.FileStore;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.BDDMockito.*;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static org.springframework.test.web.servlet.setup.MockMvcBuilders.*;

@RunWith(SpringRunner.class)
@WebMvcTest(Controller.class)
public class ControllerTest {
    @Autowired
    private MockMvc mvc;

    @MockBean
    private FileStore mockFileStore;

    private static String dummyId;
    private static Map<String, String> dummyMetadata;
    private static FileRecord dummyFileRecord;
    private static MockMultipartFile mockMultipartFile;

    @BeforeClass
    public static void setupData() {
        dummyId = "plok";

        dummyMetadata = new Hashtable<>();
        dummyMetadata.put("rab", "oof");

        dummyFileRecord = new FileRecord();
        dummyFileRecord.setId(dummyId);
        dummyFileRecord.setMetadata(dummyMetadata);
        dummyFileRecord.setStoredPath("/blah");

        mockMultipartFile = new MockMultipartFile("file", "asdfsadfasdfasdf".getBytes());
    }

    @Test
    public void testUpload() throws Exception {
        when(this.mockFileStore.storeFile(mockMultipartFile, dummyMetadata)).thenReturn(dummyId);

        MultiValueMap<String, String> params = new LinkedMultiValueMap<>();
        params.setAll(dummyMetadata);

        // Coming up roses test
        this.mvc.perform(fileUpload("/v1/upload")
                    .file(mockMultipartFile)
                    .params(params)
                    .contentType(MediaType.MULTIPART_FORM_DATA)
                )
                .andExpect(status().isOk())
                .andExpect(content().string(dummyId))
                .andDo(print());

        // Destructive test
        this.mvc.perform(fileUpload("/v1/upload")
                    .contentType(MediaType.MULTIPART_FORM_DATA)
                )
                .andExpect(status().isBadRequest())
                .andDo(print());
    }

    @Test
    public void testMetadata() throws Exception {
        when(this.mockFileStore.getFileRecord(dummyId)).thenReturn(dummyFileRecord);

        // Valid ID
        this.mvc.perform(get("/v1/metadata/"+dummyId))
                .andExpect(status().isOk())
                .andExpect(content().json(new ObjectMapper().writeValueAsString(dummyFileRecord.getMetadata())))
                .andDo(print());

        // Invalid ID
        this.mvc.perform(get("/v1/metadata/foobar"))
                .andExpect(status().isNotFound())
                .andDo(print());

        // No ID
        this.mvc.perform(get("/v1/metadata"))
                .andExpect(status().isNotFound())
                .andDo(print());
    }

    @Test
    public void testStreamFile() throws Exception {
        when(this.mockFileStore.getFileRecord(dummyId)).thenReturn(dummyFileRecord);
        Resource dummyFile = new PathResource(dummyFileRecord.getStoredPath());
        when(this.mockFileStore.getFileAsResource(dummyId)).thenReturn(dummyFile);

        this.mvc.perform(get("/v1/file/"+dummyId))
                .andExpect(status().is5xxServerError()) // This is because I still need to mock up the resource object
                .andDo(print());
    }

    @Test
    public void testSearch() throws Exception {
        List<FileRecord> returnedRecords = Arrays.asList(dummyFileRecord);

        when(this.mockFileStore.getRecordsByMetadata(dummyMetadata)).thenReturn(returnedRecords);

        MultiValueMap<String, String> params = new LinkedMultiValueMap<>();
        params.setAll(dummyMetadata);

        List<String> returnedIds = Arrays.asList(dummyId);

        this.mvc.perform(get("/v1/search")
                    .params(params)
                )
                .andExpect(status().isOk())
                .andExpect(content().json(new ObjectMapper().writeValueAsString(returnedIds)))
                .andDo(print());

        // Test with no params
        this.mvc.perform(get("/v1/search"))
                .andExpect(status().isOk())
                .andExpect(content().string("[]"))
                .andDo(print());
    }
}
