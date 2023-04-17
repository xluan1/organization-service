package com.xuanluan.mc.org.model.entity;

import com.xuanluan.mc.domain.entity.BaseEntity;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.Column;
import javax.persistence.Entity;

/**
 * @author Xuan Luan
 * @createdAt 2/4/2023
 */
@Getter
@Setter
@Entity
public class OrganizationClient extends BaseEntity {
    @Column(nullable = false, updatable = false)
    private String orgId;
    @Column(nullable = false, updatable = false)
    private String clientId;
    private String name;
    private String description;
}
