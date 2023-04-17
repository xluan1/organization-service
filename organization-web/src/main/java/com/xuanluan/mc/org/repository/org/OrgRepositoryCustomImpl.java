package com.xuanluan.mc.org.repository.org;

import com.xuanluan.mc.domain.model.filter.BaseFilter;
import com.xuanluan.mc.domain.model.filter.ResultList;
import com.xuanluan.mc.org.model.entity.Organization;
import com.xuanluan.mc.repository.BaseRepository;

import javax.persistence.EntityManager;
import javax.persistence.criteria.Predicate;
import java.util.HashMap;
import java.util.List;

/**
 * @author Xuan Luan
 * @createdAt 12/29/2022
 */
public class OrgRepositoryCustomImpl extends BaseRepository<Organization> implements OrgRepositoryCustom<Organization> {
    protected OrgRepositoryCustomImpl(EntityManager entityManager) {
        super(entityManager, Organization.class);
    }

    @Override
    public List<Organization> findAll() {
        refresh();
        List<Predicate> filters = List.of(filterNotEqualAnyField("orgId", "all"));
        return getListResult(filters);
    }

    @Override
    public ResultList<Organization> searchFullText(String orgId, BaseFilter filter) {
        refresh();
        HashMap<String, String> filterLikes = new HashMap<>();
        filterLikes.put("name", filter.getSearch());
        List<Predicate> filters = getFilterSearch(null, orgId, filterLikes, filter);
        return getResultList(filters, filter.getIndex(), filter.getMaxResult());
    }
}
