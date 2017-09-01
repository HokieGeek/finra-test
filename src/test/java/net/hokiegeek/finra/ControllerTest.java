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

import java.util.Hashtable;
import java.util.Map;

import net.hokiegeek.finra.responses.UploadResponse;
import net.hokiegeek.finra.responses.MetadataResponse;
import net.hokiegeek.finra.store.FileMetadata;

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
    private static FileMetadata dummyFileMetadata;

    @BeforeClass
    public static void setupData() {
        dummyId = "plok";

        dummyMetadata = new Hashtable<>();
        dummyMetadata.put("rab", "oof");

        dummyFileMetadata = new FileMetadata(dummyMetadata);
        dummyFileMetadata.setId(dummyId);
        dummyFileMetadata.setPath("/blah");
    }

    /*
    @Before
    public void setup() {
        mvc = MockMvcBuilders.standaloneSetup(controller).build();
    }
    */

    @Test
    @Ignore("broken")
    public void testUpload() throws Exception {
        MockMultipartFile mockMultipartFile = null;
        mockMultipartFile = new MockMultipartFile("fileName", "asdfsadfasdfasdf".getBytes());

        this.mvc.perform(fileUpload("/upload")
                    .file(mockMultipartFile)
                    .param("rab", "oof")
                    .contentType(MediaType.MULTIPART_FORM_DATA)
                )
                .andExpect(status().isCreated())
                .andExpect(content().string(dummyId));

        // given(this.controller.upload(mockMultipartFile, dummyMetadata))
        //         .willReturn(new UploadResponse(dummyId));

        // this.mvc.perform(MockMvcRequestBuilders.fileUpload("/upload")
        //             .file(mockMultipartFile)
        //             .param("rab", "oof")
        //         )
        //         .andExpect(status().isCreated())
        //         .andExpect(content().string(dummyId));
    }

    @Test
    public void testInfo() throws Exception {
        given(this.controller.metadata(dummyId))
                .willReturn(new MetadataResponse(dummyFileMetadata));

        this.mvc.perform(get("/info/"+dummyId))
                .andExpect(status().isOk());
                // .accept(MediaType.APPLICATION_JSON_UTF8)
                // .andDo(print())
                // .andExpect(content().string("foobar"));
    }
}
