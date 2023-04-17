package com.xuanluan.mc.org.model.request;

import com.xuanluan.mc.domain.model.request.FileRequest;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

/**
 * @author Xuan Luan
 * @createdAt 4/2/2023
 */
@Getter
@Setter
@Builder
public class FileEntityRequest {
    @NotBlank(message = "Loại thực thể không được để trống")
    private String entityClass;
    @NotBlank(message = "Mã thực thể không được để trống")
    private String entityId;
    @NotNull(message = "Tệp không được để trống")
    private FileRequest file;
}
