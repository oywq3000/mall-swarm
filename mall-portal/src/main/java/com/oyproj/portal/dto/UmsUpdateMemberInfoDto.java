package com.oyproj.portal.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.util.Date;


@Data
@EqualsAndHashCode(callSuper = false)
public class UmsUpdateMemberInfoDto {
    @Schema(title = "昵称")
    private String nickname;
    @Schema(title = "手机号码")
    private String phone;
    @Schema(title = "性别：0->未知；1->男；2->女")
    private Integer gender;
    @Schema(title = "生日")
    private Date birthday;
}
