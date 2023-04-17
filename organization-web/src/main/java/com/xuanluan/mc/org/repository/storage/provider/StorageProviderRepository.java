package com.xuanluan.mc.org.repository.storage.provider;

import com.xuanluan.mc.org.model.entity.StorageProvider;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

/**
 * @author Xuan Luan
 * @createdAt 12/29/2022
 */
@Repository
public interface StorageProviderRepository extends CrudRepository<StorageProvider, String>, StorageProviderRepositoryCustom {
}
