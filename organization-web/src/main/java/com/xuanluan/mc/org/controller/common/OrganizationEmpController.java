package com.xuanluan.mc.org.controller.common;

import com.xuanluan.mc.controller.BaseController;
import com.xuanluan.mc.domain.model.WrapperResponse;
import com.xuanluan.mc.domain.model.filter.ResultList;
import com.xuanluan.mc.org.model.entity.OrganizationStorage;
import com.xuanluan.mc.org.model.entity.StorageRemain;
import com.xuanluan.mc.org.model.request.UploadOrgStorageRequest;
import com.xuanluan.mc.org.model.request.filter.StorageRemainFilter;
import com.xuanluan.mc.org.service.IOrgStorageService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.Map;

/**
 * @author Xuan Luan
 * @createdAt 4/2/2023
 */
@RequiredArgsConstructor
@RestController
@RequestMapping("emp/1.0.0")
public class OrganizationEmpController extends BaseController {
    private final IOrgStorageService storageService;

    @PostMapping("upload_file_org/{orgId}")
    public WrapperResponse<Map<String, OrganizationStorage>> uploadFilesToOrgStorage(@RequestHeader String clientId, @PathVariable String orgId, @RequestBody @Valid UploadOrgStorageRequest request) {
        return response("Đăng ký thành công!", storageService.uploadFilesToOrgStorage(clientId, orgId, request));
    }

    @PostMapping("search_storage_remain/{orgId}")
    public WrapperResponse<ResultList<StorageRemain>> searchStorageRemain(@RequestHeader String clientId, @PathVariable String orgId, @RequestBody StorageRemainFilter request) {
        return response("Tìm kiếm thành công!", storageService.searchStorageRemain(clientId, orgId, request));
    }
}
