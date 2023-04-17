package com.xuanluan.mc.org.repository.storage.remain;

import com.xuanluan.mc.domain.model.filter.ResultList;
import com.xuanluan.mc.org.model.entity.StorageRemain;
import com.xuanluan.mc.org.model.request.filter.StorageRemainFilter;
import com.xuanluan.mc.repository.BaseRepository;
import com.xuanluan.mc.utils.BaseStringUtils;
import org.springframework.data.domain.Sort;

import javax.persistence.EntityManager;
import javax.persistence.criteria.JoinType;
import javax.persistence.criteria.Predicate;
import java.util.HashMap;
import java.util.List;

/**
 * @author Xuan Luan
 * @createdAt 4/2/2023
 */
public class StorageRemainRepositoryCustomImpl extends BaseRepository<StorageRemain> implements StorageRemainRepositoryCustom {
    protected StorageRemainRepositoryCustomImpl(EntityManager entityManager) {
        super(entityManager, StorageRemain.class);
    }

    @Override
    public List<StorageRemain> findAllByStorageProviderIds(String clientId, String orgId, Iterable<String> storageProviderIds) {
        refresh();
        return getListResult(appendFilter(root.get("storageProviderId").in(storageProviderIds), getFilters(clientId, orgId)));
    }

    @Override
    public List<StorageRemain> findAllGreaterThan0(String clientId, String orgId) {
        refresh();
        List<Predicate> filters = appendFilter(builder.greaterThan(root.get("remainCapacity"), 0), getFilters(clientId, orgId));
        builder.desc(root.join("provider").get("isDefault"));
        builder.asc(root.get("createdAt"));
        return getListResult(filters);
    }

    private Predicate providerJoinInner(String storageProviderId) {
        return storageProviderId != null ? builder.equal(root.join("provider", JoinType.INNER).get("id"), storageProviderId) : null;
    }

    @Override
    public StorageRemain findByStorageProviderId(String clientId, String orgId, String storageProviderId) {
        refresh();
        return getSingleResult(appendFilter(providerJoinInner(storageProviderId), getFilters(clientId, orgId)));
    }

    @Override
    public ResultList<StorageRemain> searchFullText(String clientId, String orgId, StorageRemainFilter filter) {
        refresh();
        HashMap<String, String> filterLikes = new HashMap<>();
        filterLikes.put("createdBy", filter.getSearch());
        filterLikes.put("updatedBy", filter.getSearch());
        List<Predicate> filters = getFilterSearch(clientId, orgId, filterLikes, filter);
        if (BaseStringUtils.hasTextAfterTrim(filter.getStorageProviderId())) {
            appendFilter(providerJoinInner(filter.getStorageProviderId()), filters);
        }
        return getResultList(filters, filter.getIndex(), filter.getMaxResult());
    }
}
