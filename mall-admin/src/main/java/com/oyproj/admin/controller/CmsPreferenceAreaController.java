package com.oyproj.admin.controller;

import com.oyproj.admin.model.CmsPrefrenceArea;
import com.oyproj.admin.service.CmsPreferenceAreaService;
import com.oyproj.common.api.CommonResult;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * @author oy
 * @description 商品优选管理Controller
 */
@RestController
@Tag(name = "CmsPreferenceAreaController", description = "商品优选管理")
@RequestMapping("/prefrenceArea")
@RequiredArgsConstructor
public class CmsPreferenceAreaController {
    private final CmsPreferenceAreaService preferenceAreaService;
    @Operation(summary = "获取所有商品优选")
    @RequestMapping(value = "/listAll", method = RequestMethod.GET)
    public CommonResult<List<CmsPrefrenceArea>> listAll() {
        List<CmsPrefrenceArea> prefrenceAreaList = preferenceAreaService.listAll();
        return CommonResult.success(prefrenceAreaList);
    }
}
