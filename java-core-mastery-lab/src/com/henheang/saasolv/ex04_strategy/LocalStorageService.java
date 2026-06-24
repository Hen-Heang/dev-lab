package com.henheang.saasolv.ex04_strategy;

import java.util.HashMap;
import java.util.Map;

/**
 * Mirrors saas-olv: CmmFileLocalServiceImpl
 * In Spring this is annotated:
 *   @ConditionalOnProperty(name="globals.file.storage-type", havingValue="local")
 */
public class LocalStorageService implements StorageService {

    // pretend "disk"
    private final Map<String, byte[]> disk = new HashMap<>();

    @Override public void upload(String path, byte[] content) {
        disk.put(path, content);
        System.out.println("[LOCAL] wrote " + content.length + " bytes to " + path);
    }

    @Override public byte[] download(String path) { return disk.get(path); }

    @Override public void delete(String path) {
        disk.remove(path);
        System.out.println("[LOCAL] deleted " + path);
    }

    @Override public String name() { return "LOCAL"; }
}
