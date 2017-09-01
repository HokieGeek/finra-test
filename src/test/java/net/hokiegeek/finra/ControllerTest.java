package net.hokiegeek.finra;

import org.junit.BeforeClass;
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
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static org.springframework.test.web.servlet.setup.MockMvcBuilders.*;

import java.io.InputStream;
import java.io.ByteArrayInputStream;
import java.nio.charset.StandardCharsets;

@RunWith(SpringRunner.class)
@WebMvcTest(Controller.class)
public class ControllerTest {
    @Autowired
    private MockMvc mvc;

    @MockBean
    private Controller controller;

    private static String dummyId;
    private static Map<String, String> dummyMetadata;
    private static FileMetadata dummyFileMetadata;

    @BeforeClass
    public static void setup() {
        dummyId = "plok";

        dummyMetadata = new Hashtable<>();
        dummyMetadata.put("rab", "oof");

        dummyFileMetadata = new FileMetadata(dummyMetadata);
        dummyFileMetadata.setId(dummyId);
        dummyFileMetadata.setPath("/blah");
    }

    @Test
    public void testUpload() throws Exception {
        MockMultipartFile mockMultipartFile = null;
        String mockFileStr = "sdsafdsadfsa";
        InputStream stream = new ByteArrayInputStream(mockFileStr.getBytes(StandardCharsets.UTF_8));
        mockMultipartFile = new MockMultipartFile("fileName", stream);

        given(this.controller.upload(mockMultipartFile, dummyMetadata))
                .willReturn(new UploadResponse(dummyId));

        this.mvc.perform(post("/upload").accept(MediaType.MULTIPART_FORM_DATA)
                    .file(mockMultipartFile)
                    .param("rab", "oof")
                )
                .andExpect(status().isOk()).andExpect(content().string(dummyId));
                // .andDo(print())
    }
    /*
    */

    @Test
    public void testInfo() throws Exception {
        given(this.controller.metadata(dummyId))
                .willReturn(new MetadataResponse(dummyFileMetadata));

        this.mvc.perform(get("/info/"+dummyId))
                .andExpect(status().isOk());
                // .content(MediaType.APPLICATION_JSON_UTF8)
                // .andDo(print())
                // .andExpect(content().string("foobar"));
    }
}
