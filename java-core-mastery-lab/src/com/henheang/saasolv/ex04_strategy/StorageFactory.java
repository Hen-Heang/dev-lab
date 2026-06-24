package com.henheang.saasolv.ex04_strategy;

/**
 * Simulates Spring's @ConditionalOnProperty bean selection in plain Java.
 *
 * In saas-olv, Spring reads globals.file.storage-type and registers EITHER
 * CmmFileLocalServiceImpl OR CmmFileS3ServiceImpl as the single
 * CmmFileStorageService bean. Here we do the same selection manually.
 */
public class StorageFactory {

    /**
     * @param storageType "local" or "s3" (would come from application.yml)
     */
    public static StorageService create(String storageType) {
        if ("s3".equalsIgnoreCase(storageType)) {
            return new S3StorageService("olv-bucket");
        } else if ("local".equalsIgnoreCase(storageType)) {
            return new LocalStorageService();
        }
        throw new IllegalArgumentException("Unknown storage-type: " + storageType);
    }
}
