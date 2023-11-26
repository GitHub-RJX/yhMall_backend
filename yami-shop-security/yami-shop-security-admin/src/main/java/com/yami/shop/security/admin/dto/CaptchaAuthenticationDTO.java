package com.yami.shop.security.admin.dto;

import com.yami.shop.security.common.dto.AuthenticationDTO;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

/**
 * 验证码登录
 */
@Data
public class CaptchaAuthenticationDTO extends AuthenticationDTO {

    @Schema(description = "验证码", required = true)
    private String captchaVerification;
}
