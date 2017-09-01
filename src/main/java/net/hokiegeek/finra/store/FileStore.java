package net.hokiegeek.finra.store;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

@Service
public class FileStore {
    private FileMetadataDB db = new MongoMetadataDB(); // TODO: inject
    private final ApplicationContext appContext;

    @Autowired
    public FileStore(ApplicationContext context) {
        this.appContext = context;
    }

    private static String UPLOAD_FOLDER = "/var/cache/finra-test"; // TODO: read from file

    public String storeFile(MultipartFile file) { // TODO: needs to throw IOException, I think
        String id = "None"; // TODO

        // TODO: use metadata passed in
        FileMetadata metadata = new FileMetadata();

        try {
            // Create the ID
            id = Util.getSha1FromInputStream(file.getInputStream());

            // Save the file to the upload folder
            byte[] bytes = file.getBytes();
            Path path = Paths.get(UPLOAD_FOLDER, file.getOriginalFilename());
            metadata.setPath(path.toString());
            Files.write(path, bytes);
        } catch (IOException e) {
            e.printStackTrace();
        }

        // Store the metadata to the database
        db.store(metadata);

        return id;
    }

    public FileMetadata getFileMetadata(String id) {
        return db.getById(id);
    }

    public Resource getFileAsResource(String id) {
        FileMetadata metadata = this.getFileMetadata(id);
        Resource resource = appContext.getResource("file://" + metadata.getPath());
        return resource;
    }
}
