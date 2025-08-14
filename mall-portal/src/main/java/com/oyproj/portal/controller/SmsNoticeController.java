package com.oyproj.portal.controller;


import com.oyproj.common.api.CommonResult;
import com.oyproj.mall.model.SmsNotice;
import com.oyproj.portal.dto.SmsNoticeDto;
import com.oyproj.portal.service.SmsNoticeService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@Tag(name = "SmsNoticeController",description = "消息通知控制器")
@RequestMapping("/notice")
@RequiredArgsConstructor
public class SmsNoticeController {
    private final SmsNoticeService noticeService;

    @Operation(summary = "查询所有消息")
    @GetMapping("/list")
    private CommonResult<List<SmsNotice>> list(){
        List<SmsNotice> list = noticeService.list();
        return CommonResult.success(list);
    }
}
