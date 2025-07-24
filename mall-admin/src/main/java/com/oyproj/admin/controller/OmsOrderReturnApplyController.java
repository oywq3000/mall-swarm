package com.oyproj.admin.controller;
import com.oyproj.admin.dto.OmsOrderReturnApplyResult;
import com.oyproj.admin.dto.OmsReturnApplyQueryParam;
import com.oyproj.admin.dto.OmsUpdateStatusParam;
import com.oyproj.mall.model.OmsOrderReturnApply;
import com.oyproj.admin.service.OmsOrderReturnApplyService;
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
 * @description 订单退货申请管理
 */
@RestController
@Tag(name = "OmsOrderReturnApplyController", description = "订单退货申请管理")
@RequestMapping("/returnApply")
@RequiredArgsConstructor
public class OmsOrderReturnApplyController {
    private final OmsOrderReturnApplyService returnApplyService;
    @Operation(summary = "分页查询退货申请")
    @GetMapping(value = "/list")
    public CommonResult<CommonPage<OmsOrderReturnApply>> list(OmsReturnApplyQueryParam queryParam,
                                                              @RequestParam(value = "pageSize", defaultValue = "5") Integer pageSize,
                                                              @RequestParam(value = "pageNum", defaultValue = "1") Integer pageNum) {
        IPageInfo<OmsOrderReturnApply> returnApplyList = returnApplyService.list(queryParam, pageSize, pageNum);
        return CommonResult.success(CommonPage.restPage(returnApplyList));
    }

    @Operation(summary = "批量删除申请")
    @PostMapping(value = "/delete")
    public CommonResult delete(@RequestParam("ids") List<Long> ids) {
        int count = returnApplyService.delete(ids);
        if (count > 0) {
            return CommonResult.success(count);
        }
        return CommonResult.failed();
    }

    @Operation(summary = "获取退货申请详情")
    @GetMapping(value = "/{id}")
    public CommonResult getItem(@PathVariable Long id) {
        OmsOrderReturnApplyResult result = returnApplyService.getItem(id);
        return CommonResult.success(result);
    }

    @Operation(summary = "修改申请状态")
    @PostMapping(value = "/update/status/{id}")
    public CommonResult updateStatus(@PathVariable Long id, @RequestBody OmsUpdateStatusParam statusParam) {
        int count = returnApplyService.updateStatus(id, statusParam);
        if (count > 0) {
            return CommonResult.success(count);
        }
        return CommonResult.failed();
    }

}
