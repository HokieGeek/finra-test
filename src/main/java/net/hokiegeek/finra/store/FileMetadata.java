package net.hokiegeek.finra.store;

// import org.springframework.data.annotation.Id;

public class FileMetadata {
    private Long id;

    private String path;

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }
}
