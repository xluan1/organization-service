package com.xuanluan.mc.org.repository.org_storage;

import com.xuanluan.mc.org.model.entity.OrganizationStorage;
import com.xuanluan.mc.repository.BaseRepository;

import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import java.util.ArrayList;
import java.util.List;

/**
 * @author Xuan Luan
 * @createdAt 4/1/2023
 */
public class OrgStorageRepositoryCustomImpl extends BaseRepository<OrganizationStorage> implements OrgStorageRepositoryCustom {
    protected OrgStorageRepositoryCustomImpl(EntityManager entityManager) {
        super(entityManager, OrganizationStorage.class);
    }

    @Override
    public long sumSize(String clientId, String orgId, String storageProviderId) {
        CriteriaQuery<Long> queryL = builder.createQuery(Long.class);
        Root<OrganizationStorage> rootL = queryL.from(tClass);
        List<Predicate> filters = new ArrayList<>();
        appendFilter(builder.equal(rootL.get("clientId"), clientId), filters);
        appendFilter(builder.equal(rootL.get("orgId"), orgId), filters);
        appendFilter(builder.equal(rootL.get("storageProviderId"), storageProviderId),filters);
        queryL.select(builder.sum(rootL.get("size"))).where(filters.toArray(new Predicate[]{}));
        try {
            return entityManager.createQuery(queryL).getSingleResult();
        } catch (NoResultException e) {
            return 0;
        }
    }
}
