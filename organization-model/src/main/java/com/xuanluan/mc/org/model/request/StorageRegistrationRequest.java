package com.xuanluan.mc.org.model.request;

import lombok.Getter;
import lombok.Setter;

/**
 * @author Xuan Luan
 * @createdAt 3/19/2023
 */
@Getter
@Setter
public class StorageRegistrationRequest {
    private String byUser;
    private String cStorageId;
    private String cStorageSaleId;
    private String cDiscountId;
    private boolean isDefault = false;
}
