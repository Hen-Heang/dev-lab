package com.henheang.saasolv.ex04_strategy;

import java.nio.charset.StandardCharsets;

/**
 * EXERCISE 04 — Strategy pattern selected by configuration.
 * Concept source: saas-olv CmmFileStorageService + @ConditionalOnProperty.
 *
 * The KEY lesson: the business code below never changes when you switch
 * backends — only the config value ("local" vs "s3") changes.
 */
public class Ex04Strategy {

    /** Business code depends ONLY on the interface — no idea which backend it is. */
    static void saveAvatar(StorageService storage) {
        byte[] image = "fake-image-bytes".getBytes(StandardCharsets.UTF_8);
        String key = "avatars/user-42.png";
        storage.upload(key, image);
        byte[] back = storage.download(key);
        System.out.println("  read back " + back.length + " bytes via " + storage.name());
    }

    public static void main(String[] args) {
        System.out.println("== config: storage-type=local ==");
        saveAvatar(StorageFactory.create("local"));

        System.out.println("\n== config: storage-type=s3 ==");
        saveAvatar(StorageFactory.create("s3"));

        /*
         * 🔧 PRACTICE IDEAS
         *  - Add a third strategy (e.g. InMemoryStorageService) without touching
         *    saveAvatar() — prove the Open/Closed Principle.
         *  - Read the storage-type from a real .properties file (ties into ex08).
         */
    }
}
