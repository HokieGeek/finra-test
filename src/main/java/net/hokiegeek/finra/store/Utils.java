package net.hokiegeek.finra.store;

import java.security.MessageDigest;
import java.io.InputStream;
import java.io.IOException;
import java.security.NoSuchAlgorithmException;

import javax.xml.bind.annotation.adapters.HexBinaryAdapter;

import org.springframework.web.multipart.MultipartFile;

public class Utils {
    public static String getSha1FromInputStream(InputStream inputStream) throws IllegalArgumentException, IOException, NoSuchAlgorithmException {
        if (inputStream == null) {
            throw new IllegalArgumentException("Input stream cannot be null");
        }

        MessageDigest digest = MessageDigest.getInstance("SHA-1");

        int numBytesRead = 0;
        final byte[] buffer = new byte[1024];
        while (numBytesRead != -1) {
            numBytesRead = inputStream.read(buffer);
            if (numBytesRead > 0) {
                digest.update(buffer, 0, numBytesRead);
            }
        }

        return new HexBinaryAdapter().marshal(digest.digest());
    }
}
