package com.xuanluan.mc.org.model.request.filter;

import com.xuanluan.mc.domain.model.filter.BaseFilter;
import lombok.*;

/**
 * @author Xuan Luan
 * @createdAt 4/4/2023
 */
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class StorageRemainFilter extends BaseFilter {
    private String storageProviderId;
}
