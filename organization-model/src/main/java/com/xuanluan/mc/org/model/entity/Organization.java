package com.xuanluan.mc.org.model.entity;

import com.xuanluan.mc.domain.entity.BaseEntity;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.Column;
import javax.persistence.Entity;

/**
 * @author Xuan Luan
 * @createdAt 12/24/2022
 */
@Getter
@Setter
@Entity
public class Organization extends BaseEntity {
    @Column(nullable = false, updatable = false, unique = true)
    private String orgId;
    @Column(nullable = false)
    private String name;
    private String description;
    private boolean isCustomer;
    private boolean isDefault;
    private int language;
    private boolean isAllAccess;
}
