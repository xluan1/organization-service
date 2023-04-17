package com.xuanluan.mc.org.repository.storage.remain;

import com.xuanluan.mc.domain.model.filter.ResultList;
import com.xuanluan.mc.org.model.entity.StorageRemain;
import com.xuanluan.mc.org.model.request.filter.StorageRemainFilter;

import java.util.List;

/**
 * @author Xuan Luan
 * @createdAt 4/2/2023
 */
public interface StorageRemainRepositoryCustom {
    List<StorageRemain> findAllByStorageProviderIds(String clientId, String orgId, Iterable<String> storageProviderIds);

    List<StorageRemain> findAllGreaterThan0(String clientId, String orgId);

    StorageRemain findByStorageProviderId(String clientId, String orgId, String storageProviderId);

    ResultList<StorageRemain> searchFullText(String clientId, String orgId, StorageRemainFilter filter);
}
