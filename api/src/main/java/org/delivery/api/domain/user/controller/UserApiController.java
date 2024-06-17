package org.delivery.api.domain.user.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.delivery.api.common.annotation.UserSession;
import org.delivery.api.common.api.Api;
import org.delivery.api.domain.user.business.UserBusiness;
import org.delivery.api.domain.user.controller.model.UserResponse;
import org.delivery.api.domain.user.model.User;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/user")
@Tag(name = "UserController", description = "사용자 서비스 컨트롤러")
public class UserApiController {

    private final UserBusiness userBusiness;

    @Operation(summary = "토큰 TEST")
    @GetMapping("/me")
    public Api<UserResponse> me(
            @UserSession User user
    ){
        //RequestAttributes requestContext = Objects.requireNonNull(RequestContextHolder.getRequestAttributes());
        //Object userId = requestContext.getAttribute("userId", RequestAttributes.SCOPE_REQUEST);

        UserResponse response = userBusiness.me(user);
        return Api.OK(response);
    }
}
