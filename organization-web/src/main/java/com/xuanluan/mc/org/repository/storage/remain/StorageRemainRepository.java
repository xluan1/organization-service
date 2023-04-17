package com.xuanluan.mc.org.repository.storage.remain;

import com.xuanluan.mc.org.model.entity.StorageRemain;
import org.springframework.data.repository.CrudRepository;

/**
 * @author Xuan Luan
 * @createdAt 4/2/2023
 */
public interface StorageRemainRepository extends CrudRepository<StorageRemain, String>, StorageRemainRepositoryCustom {
}
