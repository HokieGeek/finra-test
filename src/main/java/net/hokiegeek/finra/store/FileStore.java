package net.hokiegeek.finra.store;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.ApplicationContext;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Service
public class FileStore {
    @Qualifier("${metadata.database}")
    private final FileRecordDB db;
    private final ApplicationContext appContext;

    @Autowired
    public FileStore(ApplicationContext context, FileRecordDB db) {
        this.appContext = context;
        this.db = db;
    }

    @Value("${upload.location}")
    private String upload_location;

    public String storeFile(MultipartFile file, Map<String, String> metadata) throws IOException {
        String id = ""; // TODO: Better default?

        // Create the ID
        try {
            id = Utils.getSha1FromInputStream(file.getInputStream());
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }

        // Save the file to the upload folder
        byte[] bytes = file.getBytes();
        Path path = Paths.get(upload_location, id + "-" + file.getOriginalFilename());
        Files.write(path, bytes);

        // Populate the record
        FileRecord record = new FileRecord();
        record.setId(id);
        record.setMetadata(metadata);
        record.setOriginalFilename(file.getOriginalFilename());
        record.setStoredPath(path.toString());

        // Store the record in the database
        db.store(record); // TODO: check for errors?

        return id;
    }

    public FileRecord getFileRecord(String id) {
        return db.getById(id);
    }

    public Resource getFileAsResource(String id) {
        FileRecord record = this.getFileRecord(id);
        return appContext.getResource("file://" + record.getStoredPath());
    }

    public List<FileRecord> getRecordsByMetadata(String field, String value) {
        return db.getByMetadata(field, value);
    }
}
