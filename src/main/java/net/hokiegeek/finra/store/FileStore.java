package net.hokiegeek.finra.store;

import java.util.concurrent.atomic.AtomicLong;

import org.springframework.core.io.Resource;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

@Service
public class FileStore {
    private final AtomicLong nextId = new AtomicLong();

    public Long storeFile(MultipartFile file) {
        // TODO: handle the file
        return nextId.incrementAndGet();
    }

    public String getFileMetadata(Long id) {
        return "TODO";
    }

    public Resource getFileAsResource(Long id) {
        return null;
    }
}
