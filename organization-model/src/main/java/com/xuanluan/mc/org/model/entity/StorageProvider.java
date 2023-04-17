package com.xuanluan.mc.org.model.entity;

import com.xuanluan.mc.domain.entity.BaseEntity;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.Column;
import javax.persistence.Entity;

/**
 * maxSize of StorageProvider = capacity * value of unit => max size byte
 *
 * @author Xuan Luan
 * @createdAt 12/19/2022
 */
@Getter
@Setter
@Entity
public class StorageProvider extends BaseEntity {
    @Column(nullable = false, updatable = false)
    private String clientId;
    @Column(nullable = false, updatable = false)
    private String orgId;
    @Column(nullable = false)
    private String name;
    private int type;//StorageProviderType
    private String url;
    @Column(nullable = false)
    private String unit;//StorageUnit enum
    private double capacity;
    private double totalPrice;
    private double officialPrice;
    @Column(nullable = false)
    private String currency;
    @Column(nullable = false, updatable = false)
    private String cStorageId;
    @Column(nullable = false, updatable = false)
    private String cStorageSaleId;
    private String cDiscountId;
    private boolean isDefault;
}
