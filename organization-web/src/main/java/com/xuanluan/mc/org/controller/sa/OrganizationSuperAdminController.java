package com.xuanluan.mc.org.controller.sa;

import com.xuanluan.mc.controller.BaseController;
import com.xuanluan.mc.domain.model.WrapperResponse;
import com.xuanluan.mc.domain.model.filter.BaseFilter;
import com.xuanluan.mc.domain.model.filter.ResultList;
import com.xuanluan.mc.org.model.entity.Organization;
import com.xuanluan.mc.org.model.entity.OrganizationClient;
import com.xuanluan.mc.org.model.request.NewOrgClient;
import com.xuanluan.mc.org.model.request.NewOrganization;
import com.xuanluan.mc.org.service.OrganizationService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

import java.util.List;

import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;

/**
 * @author Xuan Luan
 * @createdAt 1/1/2023
 */
@RequiredArgsConstructor
@RestController
@RequestMapping("sa/1.0.0")
public class OrganizationSuperAdminController extends BaseController {
    private final OrganizationService organizationService;

    @PostMapping("create_new_organization")
    public WrapperResponse<Organization> createNewOrganization(@RequestBody @Valid NewOrganization request) {
        return response("Thêm tổ chức mới thành công!", organizationService.createNewOrganization(request));
    }

    @PostMapping("add_organization_client/{clientId}")
    public WrapperResponse<OrganizationClient> addOrganizationToClient(@PathVariable String clientId,
                                                                       @RequestBody @Valid NewOrgClient request,
                                                                       @RequestHeader(name = "X-CSRFToken") String token) {
        return response("Thêm tổ chức đến dịch vụ thành công!", organizationService.addOrganizationToClient(clientId, request, token));
    }

    @PostMapping(value = "search_organization/{orgId}", produces = {APPLICATION_JSON_VALUE})
    public WrapperResponse<ResultList<Organization>> searchOrganization(@PathVariable String orgId, @RequestBody BaseFilter request) {
        return response("Hiển thị danh sách thành công!", organizationService.searchOrganization(orgId, request));
    }

    @PostMapping(value = "search_organization_client/{clientId}/{orgId}", produces = {APPLICATION_JSON_VALUE})
    public WrapperResponse<ResultList<OrganizationClient>> searchOrganizationClient(@PathVariable String clientId,
                                                                                    @PathVariable String orgId,
                                                                                    @RequestBody BaseFilter request) {
        return response("Hiển thị danh sách thành công!", organizationService.searchOrganizationClient(clientId, orgId, request));
    }

    @GetMapping("get_all_organization/{orgId}")
    public WrapperResponse<List<Organization>> getAllOrganization(@PathVariable String orgId) {
        return response("Hiển thị danh sách tổ chức thành công", organizationService.getOrganizations(orgId));
    }
}
