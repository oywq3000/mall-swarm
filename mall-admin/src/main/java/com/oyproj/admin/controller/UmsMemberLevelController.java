package com.oyproj.admin.controller;

import com.oyproj.admin.service.UmsMemberLevelService;
import com.oyproj.common.api.CommonResult;
import com.oyproj.mall.model.UmsMemberLevel;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * @author oy
 * @description 会员等价管理Controller
 */
@RestController
@Tag(name = "UmsMemberLevelController", description = "会员等级管理")
@RequestMapping("/memberLevel")
@RequiredArgsConstructor
public class UmsMemberLevelController {
    private final UmsMemberLevelService memberLevelService;
    @GetMapping(value = "/list")
    @Operation(summary = "查询所有会员等级")
    public CommonResult<List<UmsMemberLevel>> list(@RequestParam("defaultStatus") Integer defaultStatus) {
        List<UmsMemberLevel> memberLevelList = memberLevelService.list(defaultStatus);
        return CommonResult.success(memberLevelList);
    }
}
