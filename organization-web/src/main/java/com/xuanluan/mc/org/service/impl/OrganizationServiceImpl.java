package com.xuanluan.mc.org.service.impl;

import com.xuanluan.mc.auth.model.request.UserLocalRegisterRequest;
import com.xuanluan.mc.domain.entity.DataSequence;
import com.xuanluan.mc.domain.enums.SequenceType;
import com.xuanluan.mc.domain.model.filter.BaseFilter;
import com.xuanluan.mc.domain.model.filter.ResultList;
import com.xuanluan.mc.exception.ServiceException;
import com.xuanluan.mc.org.external_api.serivces.AuthSuperAdminRestClient;
import com.xuanluan.mc.org.external_api.ExternalRestDto;
import com.xuanluan.mc.org.external_api.init.ExternalRestClientSingleton;
import com.xuanluan.mc.org.model.common.OrganizationDetail;
import com.xuanluan.mc.org.model.entity.Organization;
import com.xuanluan.mc.org.model.entity.OrganizationClient;
import com.xuanluan.mc.org.model.entity.OrganizationInfo;
import com.xuanluan.mc.org.model.entity.OrganizationLogo;
import com.xuanluan.mc.org.model.enums.EntityCharacter;
import com.xuanluan.mc.org.model.request.NewOrgClient;
import com.xuanluan.mc.org.model.request.NewOrganization;
import com.xuanluan.mc.org.repository.org.OrganizationRepository;
import com.xuanluan.mc.org.repository.org_client.OrgClientRepository;
import com.xuanluan.mc.org.repository.org_info.OrganizationInfoRepository;
import com.xuanluan.mc.org.repository.org_logo.OrgLogoRepository;
import com.xuanluan.mc.org.service.IOrgStorageService;
import com.xuanluan.mc.org.service.OrganizationService;
import com.xuanluan.mc.org.utils.MapperUtils;
import com.xuanluan.mc.service.impl.DataSequenceServiceImpl;
import com.xuanluan.mc.utils.BaseStringUtils;
import com.xuanluan.mc.utils.ExceptionUtils;
import com.xuanluan.mc.utils.GeneratorUtils;
import lombok.RequiredArgsConstructor;
import org.apache.commons.codec.binary.Base64;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

/**
 * @author Xuan Luan
 * @createdAt 12/30/2022
 */
@RequiredArgsConstructor
@Service
public class OrganizationServiceImpl implements OrganizationService {
    private final OrganizationRepository orgRepository;
    private final OrganizationInfoRepository orgInfoRepository;
    private final DataSequenceServiceImpl sequenceService;
    private final OrgClientRepository orgClientRepository;
    private final OrgLogoRepository orgLogoRepository;

    private ExternalRestDto getRestClient(String clientId) {
        return ExternalRestClientSingleton.getExternalRest(clientId);
    }

    //from services class
    private final IOrgStorageService storageService;

    @Transactional(rollbackFor = Exception.class)
    @Override
    public OrganizationClient addOrganizationToClient(final String clientId, final NewOrgClient request, final String token) {
        ExceptionUtils.invalidInput("Tên", request.getName());
        OrganizationClient orgClient = orgClientRepository.findByClientIdAndOrgId(clientId, request.getOrgId());
        if (orgClient != null) organizationAlreadyExist(request.getOrgId());

        OrganizationInfo info = orgInfoRepository.findByOrgId(request.getOrgId());
        organizationNotFound(request.getOrgId(), info);

        UserLocalRegisterRequest localRequest = new UserLocalRegisterRequest();
        localRequest.setEmail(info.getEmail());
        localRequest.setPassword(GeneratorUtils.generateRegexRandom());
        localRequest.setPassword2(localRequest.getPassword());
        //auto generate username= org+<orgId>+<username>
        String username = request.getOrgId() + GeneratorUtils.generateUsernameFromEmail(localRequest.getEmail());
        localRequest.setUsername(username);

        AuthSuperAdminRestClient authSAClient = getRestClient(clientId).getAuthSuperAdminRest();
        //status = 200 => success created
        authSAClient.createAdmin(request.getOrgId(), localRequest, token);

        //add Organization to Client
        orgClient = new OrganizationClient();
        orgClient.setOrgId(request.getOrgId());
        orgClient.setClientId(clientId);
        orgClient.setDescription(request.getDescription());
        orgClient.setName(request.getName());
        orgClientRepository.save(orgClient);

        if (request.getStorageRequest() != null && BaseStringUtils.hasTextAfterTrim(request.getStorageRequest().getCStorageId())) {
            storageService.registryStorage(clientId, request.getOrgId(), request.getStorageRequest());
        }

        return orgClient;
    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public Organization createNewOrganization(final NewOrganization request) {
        //auto generate sequence increase
        DataSequence sequence = sequenceService.generateDataSequence("all", "all", Organization.class, SequenceType.NUMBER);
        String orgId = EntityCharacter.ORGANIZATION.regex + sequence.getSequenceValue();
        OrganizationDetail detail = MapperUtils.convertToOrganizationDetail(orgId, request);
        orgInfoRepository.save(detail.getOrganizationInfo());
        if (detail.getOrganizationLogo() != null) orgLogoRepository.save(detail.getOrganizationLogo());

        return orgRepository.save(detail.getOrganization());
    }

    @Override
    public ResultList<Organization> searchOrganization(final String orgId, final BaseFilter request) {
        return orgRepository.searchFullText(orgId, request);
    }

    @Override
    public List<Organization> getOrganizations(String orgId) {
        if (!"all".equals(orgId)) {
            throw new ServiceException(HttpStatus.FORBIDDEN, "No permission access to this function", "Không có quyền truy cập đến chức năng này");
        }
        return orgRepository.findAll();
    }

    @Override
    public ResultList<OrganizationClient> searchOrganizationClient(final String clientId, final String orgId, final BaseFilter filter) {
        return orgClientRepository.searchFullText(clientId, orgId, filter);
    }


    @Override
    public Organization getOrganization(final String orgId) {
        Organization organization = orgRepository.findByOrgId(orgId);
        organizationNotFound(orgId, organization);
        return organization;
    }

    @Override
    public OrganizationInfo getOrganizationInfo(final String orgId) {
        OrganizationInfo info = orgInfoRepository.findByOrgId(orgId);
        organizationNotFound(orgId, info);
        Optional<OrganizationLogo> organizationLogo = orgLogoRepository.findById(info.getOrgLogoId());
        organizationLogo.ifPresent(logo -> {
            String base64 = Base64.encodeBase64String(logo.getData());
            info.setOrgLogoId("data:" + logo.getType() + ";base64, " + base64);
        });
        return info;
    }

    //****************** Validate ******************
    private void organizationAlreadyExist(String orgId) {
        throw new ServiceException(
                HttpStatus.CONFLICT,
                "Organization, orgId= " + orgId + " already exists in this client",
                "Tổ chức: " + orgId + ", đã tồn tại trong dịch vụ này"
        );
    }

    private void organizationNotFound(String orgId, Object data) {
        if (data == null) {
            throw new ServiceException(HttpStatus.CONFLICT, "Not found Organization, orgId= " + orgId, "Không tìm thấy tổ chức: " + orgId);
        }
    }
}
