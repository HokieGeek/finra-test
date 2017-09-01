package net.hokiegeek.finra.responses;

public class UploadResponse {
    private final String id;

    public UploadResponse(String id) {
        this.id = id;
    }

    public String getId() {
        return id;
    }
}
