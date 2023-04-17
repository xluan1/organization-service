package com.xuanluan.mc.org.model.request;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import javax.validation.Valid;
import javax.validation.constraints.NotEmpty;
import java.util.List;
import java.util.Map;

/**
 * @author Xuan Luan
 * @createdAt 4/1/2023
 */
@Getter
@Setter
@Builder
public class UploadOrgStorageRequest {
    @NotEmpty(message = "Danh sách tệp không được rỗng")
    private List<@Valid FileEntityRequest> providerFiles;
    private String byUser;
}
