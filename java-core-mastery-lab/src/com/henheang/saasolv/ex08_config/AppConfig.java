package com.henheang.saasolv.ex08_config;

/**
 * Mirrors saas-olv: egovframework.com.cmm.config.GlobalsProperties
 *
 * A typed config object. Each field maps to a key in the properties source.
 * After binding you read config with getters instead of magic strings.
 */
public class AppConfig {

    @ConfigProperty("globals.file.storage-type")
    private String storageType;

    @ConfigProperty("globals.file.s3.bucket")
    private String bucket;

    @ConfigProperty("globals.security.mode")
    private String securityMode;

    @ConfigProperty("globals.page.record-count-per-page")
    private int recordCountPerPage;

    public String getStorageType() { return storageType; }
    public String getBucket() { return bucket; }
    public String getSecurityMode() { return securityMode; }
    public int getRecordCountPerPage() { return recordCountPerPage; }

    @Override
    public String toString() {
        return "AppConfig{storageType=" + storageType
                + ", bucket=" + bucket
                + ", securityMode=" + securityMode
                + ", recordCountPerPage=" + recordCountPerPage + "}";
    }
}
