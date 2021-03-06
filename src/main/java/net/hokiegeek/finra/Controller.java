package net.hokiegeek.finra;

import java.io.IOException;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import net.hokiegeek.finra.store.FileRecord;
import net.hokiegeek.finra.store.FileStore;

@RestController
@RequestMapping({"/v1/","/"})
public class Controller {

    private static final Logger log = LoggerFactory.getLogger(Controller.class);
    private final FileStore store;

    @Autowired
    public Controller(FileStore store) {
        this.store = store;
    }

    @PostMapping("upload")
    public ResponseEntity<String> upload(@RequestPart("file") MultipartFile file,
                                                 @RequestParam Map<String, String> metadata) {
        log.info("Received file to upload: {}", file.getOriginalFilename());

        String id = null;
        try {
            id = this.store.storeFile(file, metadata);
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (id == null) {
                return ResponseEntity.notFound().build();
            } else {
                return ResponseEntity.ok(id);
            }
        }
    }

    @GetMapping("metadata/{id}")
    public ResponseEntity<Map<String, String>> metadata(@PathVariable String id) {
        log.info("Retrieving metadata for id {}", id);

        FileRecord record = this.store.getFileRecord(id);

        if (record == null) {
            return ResponseEntity.notFound().build();
        } else {
            return ResponseEntity.ok(record.getMetadata());
        }
    }

    @GetMapping("file/{id}")
    public ResponseEntity<Resource> streamFile(@PathVariable String id) {
        log.info("Retrieving file with id {}", id);

        FileRecord record = this.store.getFileRecord(id);
        if (record == null) {
            return ResponseEntity.notFound().build();
        } else {
            Resource file = this.store.getFileAsResource(id);
            Long fileContentLength = 0L;
            try {
                fileContentLength = file.contentLength();
            } catch (IOException e) {
                log.error("Could not access file's size: {}", record.getContentLength());
                return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
            }

            if (fileContentLength != record.getContentLength()) {
                log.error("Actuual file's size does not match file size in DB record");
                return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
            } else {
                return ResponseEntity.ok()
                        .header(HttpHeaders.CONTENT_DISPOSITION,
                            "attachment; filename=\"" + record.getOriginalFilename() + "\"")
                        .body(file);
            }
        }
    }

    @GetMapping("search")
    public List<String> search(@RequestParam Map<String, String> criterion) {
        log.trace("Searching by metadata");

        return this.store.getRecordsByMetadata(criterion).stream()
            .map(e -> e.getId())
            .collect(Collectors.toList());
    }
}
