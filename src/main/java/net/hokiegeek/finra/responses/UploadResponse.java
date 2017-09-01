package net.hokiegeek.finra.responses;

public class UploadResponse {
    private final long id;

    public UploadResponse(long id) {
        this.id = id;
    }

    public long getId() {
        return id;
    }
}
