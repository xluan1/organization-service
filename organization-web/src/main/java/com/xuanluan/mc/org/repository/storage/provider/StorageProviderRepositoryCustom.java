package com.xuanluan.mc.org.repository.storage.provider;

import com.xuanluan.mc.org.model.entity.StorageProvider;

import java.util.List;

/**
 * @author Xuan Luan
 * @createdAt 3/19/2023
 */
public interface StorageProviderRepositoryCustom {
    List<StorageProvider> findAll(String clientId, String orgId);

    StorageProvider getDefault(String clientId, String orgId);
}
