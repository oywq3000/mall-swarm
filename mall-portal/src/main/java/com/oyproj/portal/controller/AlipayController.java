package com.oyproj.portal.controller;

import com.alipay.api.AlipayConfig;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author oy
 * @description 支付宝支付Controller
 */
@RestController
@Tag(name = "AlipayController", description = "支付宝支付相关接口")
@RequestMapping("/alipay")
@RequiredArgsConstructor
public class AlipayController {

    private final AlipayConfig alipayConfig;
}
