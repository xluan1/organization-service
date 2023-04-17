package com.xuanluan.mc.org.model.response;

import com.xuanluan.mc.domain.enums.StorageUnit;
import com.xuanluan.mc.org.model.enums.StorageProviderType;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

/**
 * @author Xuan Luan
 * @createdAt 3/18/2023
 */
@Getter
@Setter
@Builder
public class StorageProviderResponse {
    private String name;
    private StorageProviderType type;//StorageProviderType
    private String url;
    private StorageUnit unit;//StorageUnit enum
    private double capacity;
    private long currentSize;
    private double totalPrice;
    private double officialPrice;
}
