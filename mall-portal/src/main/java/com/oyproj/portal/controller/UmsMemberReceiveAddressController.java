package com.oyproj.portal.controller;

import com.oyproj.common.api.CommonResult;
import com.oyproj.mall.model.UmsMemberReceiveAddress;
import com.oyproj.portal.service.UmsMemberReceiveAddressService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * @author oy
 * @description 会员收货地址管理Controller
 */
@RestController
@Tag(name = "UmsMemberReceiveAddressController",description = "会员收货地址管理")
@RequiredArgsConstructor
@RequestMapping("/member/address")
public class UmsMemberReceiveAddressController {

    private final UmsMemberReceiveAddressService memberReceiveAddressService;

    @Operation(summary = "添加收货地址")
    @PostMapping(value = "/add")
    public CommonResult add(@RequestBody UmsMemberReceiveAddress address) {
        int count = memberReceiveAddressService.add(address);
        if (count > 0) {
            return CommonResult.success(count);
        }
        return CommonResult.failed();
    }

    @Operation(summary = "删除收货地址")
    @PostMapping(value = "/delete/{id}")
    public CommonResult delete(@PathVariable Long id) {
        int count = memberReceiveAddressService.delete(id);
        if (count > 0) {
            return CommonResult.success(count);
        }
        return CommonResult.failed();
    }

    @Operation(summary = "修改收货地址")
    @PostMapping(value = "/update/{id}")
    public CommonResult update(@PathVariable Long id, @RequestBody UmsMemberReceiveAddress address) {
        int count = memberReceiveAddressService.update(id, address);
        if (count > 0) {
            return CommonResult.success(count);
        }
        return CommonResult.failed();
    }

    @Operation(summary = "显示所有收货地址")
    @GetMapping(value = "/list")
    public CommonResult<List<UmsMemberReceiveAddress>> list() {
        List<UmsMemberReceiveAddress> addressList = memberReceiveAddressService.list();
        return CommonResult.success(addressList);
    }

    @Operation(summary = "获取收货地址详情")
    @GetMapping(value = "/{id}")
    public CommonResult<UmsMemberReceiveAddress> getItem(@PathVariable Long id) {
        UmsMemberReceiveAddress address = memberReceiveAddressService.getItem(id);
        return CommonResult.success(address);
    }
}
