package com.oyproj.admin.controller;


import com.oyproj.admin.model.OmsCompanyAddress;
import com.oyproj.admin.service.OmsCompanyAddressService;
import com.oyproj.common.api.CommonResult;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * @author oy
 * @description 收货地址管理Controller
 */
@RestController
@Tag(name = "OmsCompanyAddressController", description = "收货地址管理")
@RequestMapping("/companyAddress")
@RequiredArgsConstructor
public class OmsCompanyAddressController {

    private final OmsCompanyAddressService companyAddressService;

    @Operation(summary = "获取所有收货地址")
    @GetMapping(value = "/list")
    public CommonResult<List<OmsCompanyAddress>> list() {
        List<OmsCompanyAddress> companyAddressList = companyAddressService.list();
        return CommonResult.success(companyAddressList);
    }
}
