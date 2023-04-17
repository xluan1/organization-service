package com.xuanluan.mc.org.controller.common;

import com.xuanluan.mc.controller.BaseController;
import com.xuanluan.mc.domain.model.WrapperResponse;
import com.xuanluan.mc.org.model.entity.StorageProvider;
import com.xuanluan.mc.org.model.request.StorageRegistrationRequest;
import com.xuanluan.mc.org.service.IOrgStorageService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

/**
 * @author Xuan Luan
 * @createdAt 3/20/2023
 */
@RequiredArgsConstructor
@RestController
@RequestMapping("ad/1.0.0")
public class OrganizationAdminController extends BaseController {
    private final IOrgStorageService storageService;

    @PostMapping("registry_storage/{clientId}/{orgId}")
    public WrapperResponse<StorageProvider> registryStorage(@PathVariable String clientId, @PathVariable String orgId, @RequestBody StorageRegistrationRequest request) {
        return response("Đăng ký thành công!", storageService.registryStorage(clientId, orgId, request));
    }
}
