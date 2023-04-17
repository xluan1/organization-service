package com.xuanluan.mc.restclient;

import com.xuanluan.mc.domain.model.WrapperResponse;
import com.xuanluan.mc.org.model.request.UploadOrgStorageRequest;
import com.xuanluan.mc.rest.BaseRestClient;
import com.xuanluan.mc.restclient.model.OrganizationStorageMap;
import com.xuanluan.mc.utils.PropertyReaderUtils;

/**
 * @author Xuan Luan
 * @createdAt 4/2/2023
 */
public class OrganizationEmpRestClient extends BaseRestClient {
    public OrganizationEmpRestClient(String clientId) {
        super(PropertyReaderUtils.getPropertyName("org-service.url") + "/emp/1.0.0/", clientId);
    }

    public WrapperResponse<OrganizationStorageMap> uploadFilesToOrgStorage(String orgId, UploadOrgStorageRequest request) {
        return postWrapper("upload_file_org/" + orgId, request, OrganizationStorageMap.class);
    }
}
