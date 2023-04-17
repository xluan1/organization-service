package com.xuanluan.mc.org.service.impl;

import com.xuanluan.mc.client.model.response.ClientStorageDetailResponse;
import com.xuanluan.mc.client.model.response.ClientStorageSaleResponse;
import com.xuanluan.mc.client.restclient.ClientRestClient;
import com.xuanluan.mc.domain.enums.StorageUnit;
import com.xuanluan.mc.domain.model.filter.ResultList;
import com.xuanluan.mc.domain.model.request.FileRequest;
import com.xuanluan.mc.exception.ServiceException;
import com.xuanluan.mc.org.external_api.ExternalRestDto;
import com.xuanluan.mc.org.external_api.init.ExternalRestClientSingleton;
import com.xuanluan.mc.org.model.entity.OrganizationStorage;
import com.xuanluan.mc.org.model.entity.StorageProvider;
import com.xuanluan.mc.org.model.entity.StorageRemain;
import com.xuanluan.mc.org.model.request.FileEntityRequest;
import com.xuanluan.mc.org.model.request.StorageRegistrationRequest;
import com.xuanluan.mc.org.model.request.UploadOrgStorageRequest;
import com.xuanluan.mc.org.model.request.filter.StorageRemainFilter;
import com.xuanluan.mc.org.repository.org_storage.OrgStorageRepository;
import com.xuanluan.mc.org.repository.storage.provider.StorageProviderRepository;
import com.xuanluan.mc.org.repository.storage.remain.StorageRemainRepository;
import com.xuanluan.mc.org.service.IOrgStorageService;
import com.xuanluan.mc.org.utils.MapperUtils;
import com.xuanluan.mc.utils.ExceptionUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;

/**
 * @author Xuan Luan
 * @createdAt 3/12/2023
 */
@RequiredArgsConstructor
@Service
public class OrgStorageServiceImpl implements IOrgStorageService {
    private final StorageProviderRepository providerRepository;
    private final OrgStorageRepository orgStorageRepository;
    private final StorageRemainRepository storageRemainRepository;

    private ExternalRestDto getClient(String clientId) {
        return ExternalRestClientSingleton.getExternalRest(clientId);
    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public StorageProvider registryStorage(final String clientId, final String orgId, final StorageRegistrationRequest request) {
        ExceptionUtils.invalidInput("Dịch vụ lưu trữ", request.getCStorageId());
        ExceptionUtils.invalidInput("Thanh toán lưu trữ", request.getCStorageSaleId());

        ClientRestClient clientRest = getClient(clientId).getClientRest();
        ClientStorageDetailResponse storageDetail = clientRest.getStorageDetail(request.getCStorageId()).getData();
        ClientStorageSaleResponse saleResponse = storageDetail.getSales().stream().filter(sale -> sale.getId().equals(request.getCStorageSaleId())).findFirst().orElse(null);

        ExceptionUtils.notFoundData("cStorageSaleId", request.getCStorageSaleId(), saleResponse);
        StorageProvider provider = MapperUtils.convertToStorageProvider(storageDetail.getStorage(), saleResponse);
        provider.setCStorageId(request.getCStorageId());
        provider.setClientId(clientId);
        provider.setOrgId(orgId);
        provider.setDefault(request.isDefault());
        if (request.isDefault()) {
            StorageProvider providerInUse = providerRepository.getDefault(clientId, orgId);
            if (providerInUse != null) {
                providerInUse.setDefault(false);
                providerRepository.save(providerInUse);
            }
        }
        // convert to byte
        StorageRemain storageRemain = MapperUtils.covertToStorageRemain(provider);
        storageRemain.setRemainCapacity((long) (provider.getCapacity() * StorageUnit.valueOf(provider.getUnit()).value));
        storageRemainRepository.save(storageRemain);
        return providerRepository.save(provider);
    }

    private Map<String, OrganizationStorage> uploadFileStorageProvider(String clientId, String orgId, List<FileEntityRequest> files, List<StorageRemain> storageRemains, String byUser) {
        final Map<String, OrganizationStorage> storageMap = new HashMap<>();
        final LinkedList<StorageRemain> remainLinkedList = new LinkedList<>(storageRemains);
        StorageRemain rmStorageRemain = null;
        for (FileEntityRequest fileEntity : files) {
            FileRequest file = fileEntity.getFile();
            long remainRequest = file.getSize();
            int indexRemain = 0;
            //calculate remain capacity after upload file
            for (StorageRemain storageRemain : remainLinkedList) {
                if (rmStorageRemain != null) {
                    remainLinkedList.remove(rmStorageRemain);
                    rmStorageRemain = null;
                }
                if (storageRemain.getRemainCapacity() > 0) {
                    if (storageRemain.getRemainCapacity() >= remainRequest) {
                        storageRemain.setRemainCapacity(storageRemain.getRemainCapacity() - remainRequest);
                        remainRequest = 0;
                    } else if (indexRemain < remainLinkedList.size() - 1) {
                        remainRequest -= storageRemain.getRemainCapacity();
                        //use StorageRemain next to calculate remain request of files
                        storageRemain.setRemainCapacity(0);
                        StorageRemain storageRemainNext = remainLinkedList.get(indexRemain + 1);
                        storageRemainNext.setRemainCapacity(storageRemainNext.getRemainCapacity() - remainRequest);
                    }
                }
                if (storageRemain.getRemainCapacity() == 0) rmStorageRemain = storageRemain;
                if (remainRequest == 0) break;
                indexRemain++;
            }
            if (remainRequest != 0) {
                throw new ServiceException(HttpStatus.PAYLOAD_TOO_LARGE, "Not enough remain capacity contains files", "Dung lượng còn lại không đủ để chứa tệp");
            }
            OrganizationStorage storage = MapperUtils.convertToOrgStorage(clientId, orgId, file);
            storage.setCreatedBy(byUser);
            storage.setEntityClass(fileEntity.getEntityClass());
            storage.setEntityId(fileEntity.getEntityId());

            storageMap.put(storage.getEntityId(), storage);
        }

        return storageMap;
    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public Map<String, OrganizationStorage> uploadFilesToOrgStorage(String clientId, String orgId, UploadOrgStorageRequest request) {
        List<StorageRemain> storageRemains = storageRemainRepository.findAllGreaterThan0(clientId, orgId);
        if (storageRemains.size() == 0) {
            throw new ServiceException(HttpStatus.NOT_FOUND, "Not found storage provider", "Không tìm thấy dịch vụ lưu trữ còn đủ sức chứa");
        }
        final Map<String, OrganizationStorage> orgStorageMap = uploadFileStorageProvider(clientId, orgId, request.getProviderFiles(), storageRemains, request.getByUser());
        ExceptionUtils.isTrue(!orgStorageMap.isEmpty(), "Lưu tệp không thành", "Save files fail");
        orgStorageRepository.saveAll(orgStorageMap.values());
        storageRemainRepository.saveAll(storageRemains);
        return orgStorageMap;
    }

    @Override
    public ResultList<StorageRemain> searchStorageRemain(String clientId, String orgId, StorageRemainFilter request) {
        return storageRemainRepository.searchFullText(clientId, orgId, request);
    }
}
