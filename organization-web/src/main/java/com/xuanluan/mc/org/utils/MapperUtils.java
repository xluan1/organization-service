package com.xuanluan.mc.org.utils;

import com.xuanluan.mc.client.model.response.ClientStorageResponse;
import com.xuanluan.mc.client.model.response.ClientStorageSaleResponse;
import com.xuanluan.mc.domain.model.request.FileRequest;
import com.xuanluan.mc.org.model.common.OrganizationDetail;
import com.xuanluan.mc.org.model.entity.*;
import com.xuanluan.mc.org.model.enums.LanguageType;
import com.xuanluan.mc.org.model.request.NewOrganization;
import org.apache.commons.codec.binary.Base64;

/**
 * @author Xuan Luan
 * @createdAt 12/30/2022
 */
public class MapperUtils {
    /**
     * convert from orgId and NewOrganization to OrganizationDetail
     *
     * @param orgId   is sequenceValue
     * @param request NewOrganization
     */
    public static OrganizationDetail convertToOrganizationDetail(String orgId, NewOrganization request) {
        Organization organization = new Organization();
        organization.setCustomer(true);
        organization.setDescription(request.getDescription());
        organization.setName(request.getName());
        organization.setOrgId(orgId);

        LanguageType languageType = LanguageType.valueOf(request.getLanguage());
        organization.setLanguage(languageType.value);
        organization.setCreatedBy(request.getCreatedBy());

        FileRequest file = request.getFile();
        OrganizationLogo logo = null;
        if (file != null) {
            logo = new OrganizationLogo();
            logo.setData(Base64.decodeBase64(file.getData()));
            logo.setName(file.getName());
            logo.setOriginFile(file.getOriginalFile());
            logo.setType(file.getType());
            logo.setCreatedBy(request.getCreatedBy());
        }

        OrganizationInfo organizationInfo = new OrganizationInfo();
        organizationInfo.setOrgId(organization.getOrgId());
        organizationInfo.setCreatedBy(organization.getCreatedBy());
        organizationInfo.setAddress(request.getAddress());
        organizationInfo.setEmail(request.getEmail());
        organizationInfo.setReferUrl(request.getReferUrl());
        organizationInfo.setLeaderName(request.getLeaderName());
        organizationInfo.setOrgLogoId(logo != null ? logo.getId() : null);

        return new OrganizationDetail(organization, organizationInfo, logo);
    }

    /**
     * convert from ClientStorageResponse, ClientStorageSaleResponse to StorageProvider
     */
    public static StorageProvider convertToStorageProvider(ClientStorageResponse storage, ClientStorageSaleResponse sale) {
        StorageProvider provider = new StorageProvider();
        provider.setName(storage.getName());
        provider.setUrl(storage.getUrl());
        provider.setCapacity(storage.getCapacity());
        provider.setType(storage.getType().value);
        provider.setUnit(storage.getUnit().name());
        provider.setTotalPrice(sale.getPrice());
        provider.setCurrency(sale.getCurrencyCode());
        provider.setCStorageSaleId(sale.getId());
        provider.setOfficialPrice(provider.getTotalPrice());

        return provider;
    }

    /**
     * convert from StorageProvider to StorageRemain
     */
    public static StorageRemain covertToStorageRemain(StorageProvider provider) {
        StorageRemain storageRemain = new StorageRemain();
        storageRemain.setClientId(provider.getClientId());
        storageRemain.setOrgId(provider.getOrgId());
//        storageRemain.setStorageProviderId(provider.getId());
        storageRemain.setCreatedBy(provider.getCreatedBy());
        storageRemain.setProvider(provider);

        return storageRemain;
    }

    /**
     * convert from FileRequest to OrganizationStorage
     */
    public static OrganizationStorage convertToOrgStorage(String clientId, String orgId, FileRequest file) {
        OrganizationStorage storage = new OrganizationStorage();
        storage.setClientId(clientId);
        storage.setOrgId(orgId);
        storage.setSize(file.getSize());
        storage.setType(file.getType());
        storage.setData(Base64.decodeBase64(file.getData()));
        storage.setName(file.getName());
        storage.setOriginFile(file.getOriginalFile());

        return storage;
    }
}
