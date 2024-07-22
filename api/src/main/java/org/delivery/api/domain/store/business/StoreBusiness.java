package org.delivery.api.domain.store.business;

import lombok.RequiredArgsConstructor;
import org.delivery.api.common.annotation.Business;
import org.delivery.api.domain.store.controller.model.StoreRegisterRequest;
import org.delivery.api.domain.store.controller.model.StoreResponse;
import org.delivery.api.domain.store.converter.StoreConverter;
import org.delivery.api.domain.store.service.StoreService;
import org.delivery.db.store.StoreEntity;
import org.delivery.db.store.enums.StoreCategory;

import java.util.List;
import java.util.Optional;

@Business
@RequiredArgsConstructor
public class StoreBusiness {

    private final StoreService storeService;
    private final StoreConverter storeConverter;

    public StoreResponse register(
            StoreRegisterRequest storeRegisterRequest
    ){
        return Optional.of(storeRegisterRequest)
                .map(storeConverter::toEntity)
                .map(storeService::register)
                .map(storeConverter::toResponse)
                .orElseThrow(null);
    }

    public List<StoreResponse> searchCategory(
            StoreCategory storeCategory
    ){
        List<StoreEntity> list = storeService.searchByCategory(storeCategory);

        return list.stream()
                .map(storeConverter::toResponse)
                .toList();
    }
}
