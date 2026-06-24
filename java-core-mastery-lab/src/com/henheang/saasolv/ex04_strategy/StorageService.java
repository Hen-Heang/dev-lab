package com.henheang.saasolv.ex04_strategy;

/**
 * Mirrors saas-olv: egovframework.com.cmm.service.CmmFileStorageService
 *
 * The STRATEGY abstraction. Callers depend only on this interface; the concrete
 * storage backend (local disk vs S3) is chosen at runtime by configuration.
 */
public interface StorageService {

    void upload(String path, byte[] content);

    byte[] download(String path);

    void delete(String path);

    /** which backend am I? (for the demo output) */
    String name();
}
