package com.oyproj.portal.controller;

import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author oy
 * @description 会员收货地址管理Controller
 */
@RestController
@Tag(name = "UmsMemberReceiveAddressController",description = "会员收货地址管理")
@RequiredArgsConstructor
@RequestMapping("/member/address")
public class UmsMemberReceiveAddressController {

}
