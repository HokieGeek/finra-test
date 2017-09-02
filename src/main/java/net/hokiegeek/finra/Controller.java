package net.hokiegeek.finra;

import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import net.hokiegeek.finra.responses.MetadataResponse;
import net.hokiegeek.finra.responses.UploadResponse;
import net.hokiegeek.finra.store.FileMetadata;
import net.hokiegeek.finra.store.FileStore;

@RestController
@RequestMapping("/v${api.version}")
public class Controller {

    private final Logger log = Logger.getLogger(Controller.class.getName());
    private final FileStore store;

    @Autowired
    public Controller(FileStore store) {
        this.store = store;
    }

    @PostMapping("/upload")
    public UploadResponse upload(@RequestParam("file") MultipartFile file,
                                 @RequestParam Map<String, String> metadata) {
        log.finest("Received file to upload: " + file.getOriginalFilename());
        // TODO: handle errors
        return new UploadResponse(this.store.storeFile(file, metadata));
    }

    @GetMapping("/metadata/{id:.*}")
    public MetadataResponse metadata(@PathVariable String id) {
        return new MetadataResponse(this.store.getFileMetadata(id));
    }

    @GetMapping("/file/{id:.*}")
    @ResponseBody
    public ResponseEntity<Resource> streamFile(@PathVariable String id) {
        Resource file = this.store.getFileAsResource(id);
        return ResponseEntity.ok().header(HttpHeaders.CONTENT_DISPOSITION,
                "attachment; filename=\"" + file.getFilename() + "\"").body(file);
    }
}
