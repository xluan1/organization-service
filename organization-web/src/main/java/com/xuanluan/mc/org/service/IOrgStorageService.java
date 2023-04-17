package com.xuanluan.mc.org.service;

import com.xuanluan.mc.domain.model.filter.ResultList;
import com.xuanluan.mc.org.model.entity.OrganizationStorage;
import com.xuanluan.mc.org.model.entity.StorageProvider;
import com.xuanluan.mc.org.model.entity.StorageRemain;
import com.xuanluan.mc.org.model.request.StorageRegistrationRequest;
import com.xuanluan.mc.org.model.request.UploadOrgStorageRequest;
import com.xuanluan.mc.org.model.request.filter.StorageRemainFilter;

import java.util.Map;

/**
 * @author Xuan Luan
 * @createdAt 3/12/2023
 */
public interface IOrgStorageService {
    StorageProvider registryStorage(String clientId, String orgId, StorageRegistrationRequest request);

    Map<String, OrganizationStorage> uploadFilesToOrgStorage(String clientId, String orgId, UploadOrgStorageRequest request);

    ResultList<StorageRemain> searchStorageRemain(String clientId, String orgId, StorageRemainFilter request);
}
