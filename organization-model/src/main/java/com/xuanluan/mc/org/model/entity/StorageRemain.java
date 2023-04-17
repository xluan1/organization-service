package com.xuanluan.mc.org.model.entity;

import com.xuanluan.mc.domain.entity.BaseEntity;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;

/**
 * @author Xuan Luan
 * @createdAt 4/2/2023
 */
@Getter
@Setter
@Entity
public class StorageRemain extends BaseEntity {
    @Column(nullable = false)
    private String clientId;
    @Column(nullable = false)
    private String orgId;
    private long remainCapacity;
    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "storageProviderId", referencedColumnName = "id", nullable = false)
    private StorageProvider provider;
}
