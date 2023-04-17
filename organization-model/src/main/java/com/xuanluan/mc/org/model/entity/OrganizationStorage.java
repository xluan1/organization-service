package com.xuanluan.mc.org.model.entity;

import com.xuanluan.mc.domain.entity.BaseEntity;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Lob;

/**
 * @author Xuan Luan
 * @createdAt 12/30/2022
 */
@Getter
@Setter
@Entity
public class OrganizationStorage extends BaseEntity {
    @Column(nullable = false, updatable = false)
    private String clientId;
    @Column(nullable = false, updatable = false)
    private String orgId;
    @Column(nullable = false, updatable = false)
    private String storageProviderId;
    @Column(nullable = false, updatable = false)
    private String type; // png/jpg/...
    private String name;
    private String originFile;
    @Column(updatable = false)
    private long size;
    @Lob
    private byte[] data;
    @Column(nullable = false, updatable = false)
    private String entityId;
    @Column(nullable = false, updatable = false)
    private String entityClass;
}
