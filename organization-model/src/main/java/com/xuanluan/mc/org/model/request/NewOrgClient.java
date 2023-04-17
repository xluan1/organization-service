package com.xuanluan.mc.org.model.request;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.NotBlank;

/**
 * @author Xuan Luan
 * @createdAt 2/5/2023
 */
@Getter
@Setter
@Builder
public class NewOrgClient {
    @NotBlank(message = "Xin vui lòng chọn tổ chức")
    private String orgId;
    private String byUser;
    private String name;
    private String description;

    private StorageRegistrationRequest storageRequest;
}
