package net.hokiegeek.finra.store;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.ApplicationContext;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Repository;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

@Repository
public class FileStore {
    private final ApplicationContext appContext;

    @Qualifier("${metadata.database}")
    private final FileRecordDB db;

    @Value("${upload.location}")
    private String upload_location;

    @Autowired
    public FileStore(ApplicationContext context, FileRecordDB db) {
        this.appContext = context;
        this.db = db;
    }

    public synchronized String storeFile(MultipartFile file, Map<String, String> metadata) throws IOException {
        String id = null;

        // Create the ID
        try {
            id = Utils.getSha1FromInputStream(file.getInputStream());
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }

        // Save the file to the upload folder
        Path path = Paths.get(upload_location, id + "-" + file.getOriginalFilename());
        try {
            file.transferTo(new File(path.toString()));
        } catch (java.lang.IllegalStateException e) {
            e.printStackTrace();
        }

        // Populate the record
        FileRecord record = new FileRecord();
        record.setId(id);
        record.setStoredTimestamp(new Date());
        record.setMetadata(metadata);
        record.setOriginalFilename(file.getOriginalFilename());
        record.setContentLength(file.getSize());
        record.setStoredPath(path.toString());

        // Store the record in the database
        db.store(record); // TODO: check for errors?

        return id;
    }

    public synchronized boolean deleteFileById(String id) {
        FileRecord record = this.getFileRecord(id);

        try {
            db.delete(id);
        } catch (IllegalArgumentException e) {
            e.printStackTrace();
        }

        boolean deleted = false;
        try {
            deleted = Files.deleteIfExists(Paths.get(record.getStoredPath()));
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            return deleted;
        }
    }

    public synchronized Long count() {
        return db.count();
    }

    public synchronized List<FileRecord> getAllRecords() {
        return db.getAll();
    }

    public synchronized FileRecord getFileRecord(String id) {
        return db.getById(id);
    }

    public synchronized Resource getFileAsResource(String id) {
        FileRecord record = this.getFileRecord(id);
        return appContext.getResource("file://" + record.getStoredPath());
    }

    public synchronized List<FileRecord> getRecordsByMetadata(Map<String, String> metadata) {
        return db.getByMetadata(metadata);
    }
}
