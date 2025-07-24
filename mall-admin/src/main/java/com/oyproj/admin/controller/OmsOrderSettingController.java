package com.oyproj.admin.controller;

import com.oyproj.mall.model.OmsOrderSetting;
import com.oyproj.admin.service.OmsOrderSettingService;
import com.oyproj.common.api.CommonResult;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

/**
 * @author oy
 * @description 订单设置Controller
 */
@RestController
@Tag(name = "OmsOrderSettingController", description = "订单设置管理")
@RequestMapping("/orderSetting")
@RequiredArgsConstructor
public class OmsOrderSettingController {
    private final OmsOrderSettingService orderSettingService;
    @Operation(summary = "获取指定订单设置")
    @GetMapping(value = "/{id}")
    public CommonResult<OmsOrderSetting> getItem(@PathVariable Long id) {
        OmsOrderSetting orderSetting = orderSettingService.getItem(id);
        return CommonResult.success(orderSetting);
    }

    @Operation(summary = "修改指定订单设置")
    @PostMapping(value = "/update/{id}")
    public CommonResult update(@PathVariable Long id, @RequestBody OmsOrderSetting orderSetting) {
        int count = orderSettingService.update(id,orderSetting);
        if(count>0){
            return CommonResult.success(count);
        }
        return CommonResult.failed();
    }
}
