package com.henheang.saasolv.ex04_strategy;

import java.util.HashMap;
import java.util.Map;

/**
 * Mirrors saas-olv: CmmFileS3ServiceImpl (AWS SDK S3, NCP Object Storage)
 * In Spring this is annotated:
 *   @ConditionalOnProperty(name="globals.file.storage-type", havingValue="s3")
 */
public class S3StorageService implements StorageService {

    private final String bucket;
    private final Map<String, byte[]> objectStore = new HashMap<>(); // pretend S3

    public S3StorageService(String bucket) {
        this.bucket = bucket;
    }

    @Override public void upload(String path, byte[] content) {
        objectStore.put(path, content);
        System.out.println("[S3] putObject bucket=" + bucket + " key=" + path
                + " (" + content.length + " bytes)");
    }

    @Override public byte[] download(String path) { return objectStore.get(path); }

    @Override public void delete(String path) {
        objectStore.remove(path);
        System.out.println("[S3] deleteObject bucket=" + bucket + " key=" + path);
    }

    @Override public String name() { return "S3(" + bucket + ")"; }
}
