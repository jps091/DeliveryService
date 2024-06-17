package org.delivery.api.domain.user.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.delivery.api.common.api.Api;
import org.delivery.api.domain.token.controller.model.TokenResponse;
import org.delivery.api.domain.user.business.UserBusiness;
import org.delivery.api.domain.user.controller.model.UserLoginRequest;
import org.delivery.api.domain.user.controller.model.UserRegisterRequest;
import org.delivery.api.domain.user.controller.model.UserResponse;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/open-api/user")
@Tag(name = "OpenController", description = "개방된 서비스 컨트롤롤러")
public class UserOpenApiController {

    private final UserBusiness userBusiness;

    @Operation(summary = "회원 등록 API", description = "이름, 이메일, 패스워드, 거주지역 입력")
    @PostMapping("/register")
    public Api<UserResponse> register(
            @Valid
            @RequestBody Api<UserRegisterRequest> request
    ){
        UserResponse response = userBusiness.register(request.getBody());
        return Api.OK(response);
    }

    @Operation(summary = "회원 로그인 API", description = "이메일, 패스워드 입력")
    @PostMapping("/login")
    public Api<TokenResponse> login(
            @Valid
            @RequestBody Api<UserLoginRequest> request
    ){
        TokenResponse response = userBusiness.login(request.getBody());
        return Api.OK(response);
    }
}
