package com.oyproj.admin.controller;

import com.oyproj.mall.model.CmsSubject;
import com.oyproj.admin.service.CmsSubjectService;
import com.oyproj.common.api.CommonPage;
import com.oyproj.common.api.CommonResult;
import com.oyproj.common.api.IPageInfo;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * @author oy
 * @description 商品专题管理Controller
 */
@RestController
@Tag(name = "CmsSubjectController", description = "商品专题管理")
@RequestMapping("/subject")
@RequiredArgsConstructor
public class CmsSubjectController {
    private final CmsSubjectService subjectService;
    @Operation(summary = "获取全部商品专题")
    @GetMapping(value = "/listAll")
    public CommonResult<List<CmsSubject>> listAll() {
        List<CmsSubject> subjectList = subjectService.listAll();
        return CommonResult.success(subjectList);
    }

    @Operation(summary = "根据专题名称分页获取专题")
    @GetMapping(value = "/list")
    public CommonResult<CommonPage<CmsSubject>> getList(@RequestParam(value = "keyword", required = false) String keyword,
                                                        @RequestParam(value = "pageNum", defaultValue = "1") Integer pageNum,
                                                        @RequestParam(value = "pageSize", defaultValue = "5") Integer pageSize) {
        IPageInfo<CmsSubject> subjectList = subjectService.list(keyword, pageNum, pageSize);
        return CommonResult.success(CommonPage.restPage(subjectList));
    }
}
