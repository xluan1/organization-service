package com.xuanluan.mc.org.repository.storage.provider;

import com.xuanluan.mc.org.model.entity.StorageProvider;
import com.xuanluan.mc.repository.BaseRepository;

import javax.persistence.EntityManager;
import java.util.List;

/**
 * @author Xuan Luan
 * @createdAt 3/19/2023
 */
public class StorageProviderRepositoryCustomImpl extends BaseRepository<StorageProvider> implements StorageProviderRepositoryCustom {
    protected StorageProviderRepositoryCustomImpl(EntityManager entityManager) {
        super(entityManager, StorageProvider.class);
    }

    @Override
    public List<StorageProvider> findAll(String clientId, String orgId) {
        return null;
    }

    @Override
    public StorageProvider getDefault(String clientId, String orgId) {
        builder = entityManager.getCriteriaBuilder();
        query = builder.createQuery(tClass);
        root = query.from(tClass);
        return getSingleResult(appendFilter("isDefault", true, getFilters(clientId, orgId)));
    }
}
