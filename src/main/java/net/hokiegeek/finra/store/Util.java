package net.hokiegeek.finra.store;

import java.util.concurrent.atomic.AtomicLong;

import java.io.InputStream;

import org.springframework.web.multipart.MultipartFile;

public class Util {
    private static final AtomicLong nextId = new AtomicLong();
    public static String getSha1FromInputStream(InputStream stream) {
        // TODO: you know... do it correctly..
        Long id = nextId.incrementAndGet();
        return id.toString();
    }
}
