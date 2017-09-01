package net.hokiegeek.finra.store;

import java.util.concurrent.atomic.AtomicLong;

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
    private final AtomicLong nextId = new AtomicLong();
    private final ApplicationContext appContext;

    @Autowired
    public FileStore(ApplicationContext context) {
        this.appContext = context;
    }

    private static String UPLOAD_FOLDER = "/var/cache/finra-test";

    private FileMetadata dummyMetadata = null;

    public Long storeFile(MultipartFile file) {
        // TODO: handle the file
        Long id = nextId.incrementAndGet();

        FileMetadata metadata = new FileMetadata();

        // Save the file to the upload folder
        try {
            byte[] bytes = file.getBytes();
            Path path = Paths.get(UPLOAD_FOLDER, file.getOriginalFilename());
            metadata.setPath(path.toString());
            Files.write(path, bytes);
        } catch (IOException e) {
            e.printStackTrace();
        }

        dummyMetadata = metadata;

        return id;
    }

    public FileMetadata getFileMetadata(Long id) {
        return dummyMetadata;
    }

    public Resource getFileAsResource(Long id) {
        FileMetadata metadata = this.getFileMetadata(id);
        Resource resource = appContext.getResource("file://" + metadata.getPath());
        return resource;
    }
}
