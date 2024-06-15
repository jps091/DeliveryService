package org.delivery.api.common.error;


import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public enum UserErrorCode implements ErrorCodeIfs{

    USER_NOT_FOUND(404, 1404, "사용자를 찾을 수 없습니다."),
    ;

    private final Integer httpStatusCode;
    private final Integer errorCode;
    private final String description;
}
