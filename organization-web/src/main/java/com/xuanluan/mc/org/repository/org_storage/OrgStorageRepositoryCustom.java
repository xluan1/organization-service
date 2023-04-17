package com.xuanluan.mc.org.repository.org_storage;

/**
 * @author Xuan Luan
 * @createdAt 4/1/2023
 */
public interface OrgStorageRepositoryCustom {
    long sumSize(String clientId, String orgId, String storageProviderId);
}
