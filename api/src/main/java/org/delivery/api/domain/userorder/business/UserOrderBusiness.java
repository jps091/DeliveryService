package org.delivery.api.domain.userorder.business;

import lombok.RequiredArgsConstructor;
import org.delivery.api.common.annotation.Business;
import org.delivery.api.domain.store.converter.StoreConverter;
import org.delivery.api.domain.store.service.StoreService;
import org.delivery.api.domain.storemenu.converter.StoreMenuConverter;
import org.delivery.api.domain.storemenu.service.StoreMenuService;
import org.delivery.api.domain.user.model.User;
import org.delivery.api.domain.userorder.controller.model.UserOrderDetailResponse;
import org.delivery.api.domain.userorder.controller.model.UserOrderRequest;
import org.delivery.api.domain.userorder.controller.model.UserOrderResponse;
import org.delivery.api.domain.userorder.converter.UserOrderConverter;
import org.delivery.api.domain.userorder.service.UserOrderService;
import org.delivery.api.domain.userordermenu.converter.UserOrderMenuConverter;
import org.delivery.api.domain.userordermenu.service.UserOrderMenuService;
import org.delivery.db.store.StoreEntity;
import org.delivery.db.storemenu.StoreMenuEntity;
import org.delivery.db.userorder.UserOrderEntity;
import org.delivery.db.userordermenu.UserOrderMenuEntity;

import java.util.List;

@Business
@RequiredArgsConstructor
public class UserOrderBusiness {

    private final UserOrderService userOrderService;
    private final UserOrderConverter userOrderConverter;
    private final UserOrderMenuService userOrderMenuService;
    private final UserOrderMenuConverter userOrderMenuConverter;
    private final StoreMenuService storeMenuService;
    private final StoreMenuConverter storeMenuConverter;
    private final StoreService storeService;
    private final StoreConverter storeConverter;


    public UserOrderResponse userOrder(User user, UserOrderRequest body){

        List<StoreMenuEntity> storeMenuEntityList = body.getStoreMenuIdList()
                .stream()
                .map(storeMenuService::getStoreMenuWithThrow)
                .toList();

        UserOrderEntity userOrderEntity = userOrderConverter.toEntity(user, storeMenuEntityList);
        UserOrderEntity newUserOrder = userOrderService.order(userOrderEntity);

        List<UserOrderMenuEntity> userOrderMenuEntityList = storeMenuEntityList.stream()
                .map(it -> {
                    UserOrderMenuEntity userOrderMenuEntity = userOrderMenuConverter.toEntity(newUserOrder, it);
                    return userOrderMenuEntity;
                })
                .toList();

        userOrderMenuEntityList.forEach(userOrderMenuService::order);

        return userOrderConverter.toResponse(newUserOrder);
    }

    public List<UserOrderDetailResponse> current(User user){

        List<UserOrderEntity> userOrderEntityList = userOrderService.current(user.getId());

        List<UserOrderDetailResponse> userOrderDetailResponseList = userOrderEntityList.stream().map(it -> {

            // 사용자가 주문한 메뉴
            List<UserOrderMenuEntity> userOrderMenuEntityList = userOrderMenuService.getUserOrderMenu(it.getId());

            List<StoreMenuEntity> storeMenuEntityList = userOrderMenuEntityList.stream()
                    .map(userOrderMenuEntity -> {
                        return storeMenuService.getStoreMenuWithThrow(userOrderMenuEntity.getStoreMenuId());
                    })
                    .toList();

            StoreEntity storeEntity = storeService.getStoreWithThrow(storeMenuEntityList.stream().findFirst().get().getStoreId());


            return UserOrderDetailResponse.builder()
                    .userOrderResponse(userOrderConverter.toResponse(it))
                    .storeResponse(storeConverter.toResponse(storeEntity))
                    .storeMenuResponseList(storeMenuConverter.toResponse(storeMenuEntityList))
                    .build();
        }).toList();

        return userOrderDetailResponseList;
    }

    public List<UserOrderDetailResponse> history(User user) {

        List<UserOrderEntity> userOrderEntityList = userOrderService.history(user.getId());

        List<UserOrderDetailResponse> userOrderDetailResponseList = userOrderEntityList.stream().map(it -> {

            //사용자가 주문한 메뉴
            List<UserOrderMenuEntity> userOrderMenuEntityList = userOrderMenuService.getUserOrderMenu(it.getUserId());


            // 메뉴 디테일 정보
            List<StoreMenuEntity> storeMenuEntityList = userOrderMenuEntityList.stream()
                    .map(userOrderMenuEntity -> {
                        return storeMenuService.getStoreMenuWithThrow(userOrderMenuEntity.getStoreMenuId());
                    })
                    .toList();

            StoreEntity storeEntity = storeService.getStoreWithThrow(storeMenuEntityList.stream().findFirst().get().getStoreId());

            return UserOrderDetailResponse.builder()
                    .userOrderResponse(userOrderConverter.toResponse(it))
                    .storeMenuResponseList(storeMenuConverter.toResponse(storeMenuEntityList))
                    .storeResponse(storeConverter.toResponse(storeEntity))
                    .build()
                    ;
        }).toList();

        return  userOrderDetailResponseList;
    }

    public UserOrderDetailResponse readOne(User user, Long orderId){

        UserOrderEntity userOrderEntity = userOrderService.getUserOrderWithOutStatusWithThrow(orderId, user.getId());

        //사용자가 주문한 메뉴
        List<UserOrderMenuEntity> userOrderMenuEntityList = userOrderMenuService.getUserOrderMenu(userOrderEntity.getId());

        List<StoreMenuEntity> storeMenuEntityList = userOrderMenuEntityList.stream()
                .map(userOrderMenuEntity -> {
                    StoreMenuEntity storeMenuEntity = storeMenuService.getStoreMenuWithThrow(userOrderMenuEntity.getStoreMenuId());
                    return storeMenuEntity;
                })
                .toList();

        StoreEntity storeEntity = storeService.getStoreWithThrow(storeMenuEntityList.stream().findFirst().get().getStoreId());


        return UserOrderDetailResponse.builder()
                .userOrderResponse(userOrderConverter.toResponse(userOrderEntity))
                .storeMenuResponseList(storeMenuConverter.toResponse(storeMenuEntityList))
                .storeResponse(storeConverter.toResponse(storeEntity))
                .build()
                ;
    }
}
