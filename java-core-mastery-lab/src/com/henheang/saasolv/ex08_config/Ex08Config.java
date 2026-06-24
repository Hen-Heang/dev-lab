package com.henheang.saasolv.ex08_config;

import java.util.Properties;

/**
 * EXERCISE 08 — Type-safe config binding via reflection.
 * Concept source: saas-olv GlobalsProperties (@ConfigurationProperties).
 */
public class Ex08Config {

    public static void main(String[] args) throws Exception {
        // pretend this came from application.yml / application.properties
        Properties props = new Properties();
        props.setProperty("globals.file.storage-type", "s3");
        props.setProperty("globals.file.s3.bucket", "olv-prod-bucket");
        props.setProperty("globals.security.mode", "web");
        props.setProperty("globals.page.record-count-per-page", "20");

        AppConfig config = PropertyBinder.bind(AppConfig.class, props);

        System.out.println(config);
        System.out.println("\nread type-safely (no magic strings):");
        System.out.println("  storageType        = " + config.getStorageType());
        System.out.println("  bucket             = " + config.getBucket());
        System.out.println("  recordCountPerPage = " + config.getRecordCountPerPage()
                + " (note: bound as int, not String)");

        /*
         * 🔧 PRACTICE IDEAS
         *  - Load the Properties from a real file via props.load(InputStream).
         *  - Feed config.getStorageType() into ex04's StorageFactory to wire the
         *    two exercises together (config -> strategy selection).
         *  - Support nested objects (e.g. an S3 sub-object) like GlobalsProperties.
         */
    }
}
