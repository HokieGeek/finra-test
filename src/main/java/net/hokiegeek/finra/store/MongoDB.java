package net.hokiegeek.finra.store;

import org.springframework.data.mongodb.repository.MongoRepository;

public interface MongoDB extends MongoRepository<FileMetadata, String> {
    // .... well, this is weird ... I mean, I get it... but still feels weird
    public FileMetadata findById(String id);
}
