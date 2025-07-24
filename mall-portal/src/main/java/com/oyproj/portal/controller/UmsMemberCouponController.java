package com.oyproj.portal.controller;

import com.oyproj.mall.model.SmsCoupon;
import com.oyproj.mall.model.SmsCouponHistory;
import com.oyproj.common.api.CommonResult;
import com.oyproj.portal.dto.CartPromotionItem;
import com.oyproj.portal.dto.SmsCouponHistoryDetail;
import com.oyproj.portal.service.OmsCartItemService;
import com.oyproj.portal.service.UmsMemberCouponService;
import com.oyproj.portal.service.UmsMemberService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.enums.ParameterIn;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@Tag(name = "UmsMemberCouponController", description = "用户优惠券管理")
@RequestMapping("/member/coupon")
@RequiredArgsConstructor
public class UmsMemberCouponController {
    private final UmsMemberCouponService memberCouponService;

    private final OmsCartItemService cartItemService;

    private final UmsMemberService memberService;

    @Operation(summary = "领取指定优惠券")
    @PostMapping(value = "/add/{couponId}")
    public CommonResult add(@PathVariable Long couponId) {
        memberCouponService.add(couponId);
        return CommonResult.success(null,"领取成功");
    }

    @Operation(summary = "获取会员优惠券历史列表")
    @Parameter(name = "useStatus", description = "优惠券筛选类型:0->未使用；1->已使用；2->已过期",
            in = ParameterIn.QUERY,schema = @Schema(type = "integer",allowableValues = {"0","1","2"}))
    @GetMapping(value = "/listHistory")
    public CommonResult<List<SmsCouponHistory>> listHistory(@RequestParam(value = "useStatus", required = false) Integer useStatus) {
        List<SmsCouponHistory> couponHistoryList = memberCouponService.listHistory(useStatus);
        return CommonResult.success(couponHistoryList);
    }

    @Operation(summary = "获取会员优惠券列表")
    @Parameter(name = "useStatus", description = "优惠券筛选类型:0->未使用；1->已使用；2->已过期",
            in = ParameterIn.QUERY,schema = @Schema(type = "integer",allowableValues = {"0","1","2"}))
    @GetMapping(value = "/list")
    public CommonResult<List<SmsCoupon>> list(@RequestParam(value = "useStatus", required = false) Integer useStatus) {
        List<SmsCoupon> couponList = memberCouponService.list(useStatus);
        return CommonResult.success(couponList);
    }

    @Operation(summary = "获取登录会员购物车的相关优惠券")
    @Parameter(name = "type", description = "使用可用:0->不可用；1->可用",
            in = ParameterIn.PATH,schema = @Schema(type = "integer",defaultValue = "1",allowableValues = {"0","1"}))
    @GetMapping(value = "/list/cart/{type}")
    public CommonResult<List<SmsCouponHistoryDetail>> listCart(@PathVariable Integer type) {
        List<CartPromotionItem> cartPromotionItemList = cartItemService.listPromotion(memberService.getCurrentMember().getId(), null);
        List<SmsCouponHistoryDetail> couponHistoryList = memberCouponService.listCart(cartPromotionItemList, type);
        return CommonResult.success(couponHistoryList);
    }

    @Operation(summary = "获取当前商品相关优惠券")
    @GetMapping(value = "/listByProduct/{productId}")
    public CommonResult<List<SmsCoupon>> listByProduct(@PathVariable Long productId) {
        List<SmsCoupon> couponHistoryList = memberCouponService.listByProduct(productId);
        return CommonResult.success(couponHistoryList);
    }
}
