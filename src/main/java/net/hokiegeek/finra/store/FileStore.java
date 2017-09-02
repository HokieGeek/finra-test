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

    public String storeFile(MultipartFile file, Map<String, String> metadata) { // TODO: needs to throw IOException, I think
        String id = "None"; // TODO

        FileRecord record = new FileRecord(metadata);

        try {
            // Create the ID
            id = Utils.getSha1FromInputStream(file.getInputStream());
            record.setId(id);

            // Save the file to the upload folder
            byte[] bytes = file.getBytes();
            Path path = Paths.get(upload_location, file.getOriginalFilename());
            Files.write(path, bytes);

            record.setPath(path.toString());
        } catch (IOException e) {
            e.printStackTrace();
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }

        // Store the record to the database
        db.store(record);

        return id;
    }

    public FileRecord getFileRecord(String id) {
        return db.getById(id);
    }

    public Resource getFileAsResource(String id) {
        FileRecord record = this.getFileRecord(id);
        Resource resource = appContext.getResource("file://" + record.getPath());
        return resource;
    }
}
