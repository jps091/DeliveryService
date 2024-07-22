package org.delivery.api.domain.store.controller;

import lombok.RequiredArgsConstructor;
import org.delivery.api.common.api.Api;
import org.delivery.api.domain.store.business.StoreBusiness;
import org.delivery.api.domain.store.controller.model.StoreResponse;
import org.delivery.db.store.enums.StoreCategory;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/***
 * 1. service 구성 (엔티티로만 로직구성)
 * 2. Model(Dto) 구성
 * 3. Business 구성
 * 4. 컨트롤러 구성
 */

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/store")
public class StoreApiController {


        private final StoreBusiness storeBusiness;

        @GetMapping("/search")
        public Api<List<StoreResponse>> search(
                @RequestParam(required = false)
                StoreCategory storeCategory
        ){
            List<StoreResponse> response = storeBusiness.searchCategory(storeCategory);
            return Api.OK(response);
        }
}
