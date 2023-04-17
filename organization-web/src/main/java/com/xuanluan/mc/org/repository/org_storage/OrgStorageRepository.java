package com.xuanluan.mc.org.repository.org_storage;

import com.xuanluan.mc.org.model.entity.OrganizationStorage;
import org.springframework.data.repository.CrudRepository;

/**
 * @author Xuan Luan
 * @createdAt 4/1/2023
 */
public interface OrgStorageRepository extends CrudRepository<OrganizationStorage, String>, OrgStorageRepositoryCustom {
}
