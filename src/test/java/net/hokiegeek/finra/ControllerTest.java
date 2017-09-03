package net.hokiegeek.finra;

import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Ignore;
import org.junit.Test;
import org.junit.runner.RunWith;

import org.springframework.beans.factory.annotation.*;
import org.springframework.boot.test.autoconfigure.web.servlet.*;
import org.springframework.boot.test.mock.mockito.*;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.util.MultiValueMap;
import org.springframework.util.LinkedMultiValueMap;

import java.util.Hashtable;
import java.util.List;
import java.util.Map;

import net.hokiegeek.finra.responses.UploadResponse;
import net.hokiegeek.finra.responses.MetadataResponse;
import net.hokiegeek.finra.store.FileRecord;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.BDDMockito.*;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static org.springframework.test.web.servlet.setup.MockMvcBuilders.*;

@RunWith(SpringRunner.class)
@WebMvcTest(Controller.class)
@AutoConfigureMockMvc
public class ControllerTest {
    @Autowired
    private MockMvc mvc;

    @MockBean
    private Controller controller;

    private static String dummyId;
    private static Map<String, String> dummyMetadata;
    private static FileRecord dummyFileRecord;

    @BeforeClass
    public static void setupData() {
        dummyId = "plok";

        dummyMetadata = new Hashtable<>();
        dummyMetadata.put("rab", "oof");

        dummyFileRecord = new FileRecord();
        dummyFileRecord.setId(dummyId);
        dummyFileRecord.setMetadata(dummyMetadata);
        dummyFileRecord.setStoredPath("/blah");
    }

    @Test
    public void testUpload() throws Exception {
        MockMultipartFile mockMultipartFile = null;
        mockMultipartFile = new MockMultipartFile("file", "asdfsadfasdfasdf".getBytes());

        MultiValueMap<String, String> params = new LinkedMultiValueMap<>();
        params.setAll(dummyMetadata);

        // given(this.controller.upload(mockMultipartFile, dummyMetadata))
        //         .willReturn(new UploadResponse(dummyId));

        this.mvc.perform(fileUpload("/v1/upload")
                    .file(mockMultipartFile)
                    .params(params)
                    .contentType(MediaType.MULTIPART_FORM_DATA)
                )
                .andExpect(status().isOk())
                // .andExpect(content().string(dummyId))
                ;
    }

    @Test
    public void testMetadata() throws Exception {
        MetadataResponse response = new MetadataResponse();
        response.setMetadata(dummyFileRecord.getMetadata());

        // given(this.controller.metadata(dummyId)).willReturn(response);

        this.mvc.perform(get("/v1/metadata/"+dummyId))
                .andExpect(status().isOk());
                // .accept(MediaType.APPLICATION_JSON_UTF8)
                // .andExpect(content().string("foobar"));
    }

    @Test
    @Ignore("TODO")
    public void testFileDownload() throws Exception {
        // given(this.controller.metadata(dummyId))
        //         .willReturn(new MetadataResponse(dummyFileRecord));

        this.mvc.perform(get("/v1/file/"+dummyId))
                .andExpect(status().isOk());
                // .accept(MediaType.APPLICATION_JSON_UTF8)
                // .andDo(print())
                // .andExpect(content().string("foobar"));
    }

    @Test
    @Ignore("TODO")
    public void testSearch() throws Exception {
        // given(this.controller.metadata(dummyId))
        //         .willReturn(new MetadataResponse(dummyFileRecord));

        this.mvc.perform(post("/v1/search")
                .param("", "")
                ) ;
                // .andExpect(status().isOk())
                // .accept(MediaType.APPLICATION_JSON_UTF8)
                // .andDo(print())
                // .andExpect(content().string("foobar"));
    }
}
