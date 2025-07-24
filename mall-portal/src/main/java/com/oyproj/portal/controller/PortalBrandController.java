package com.oyproj.portal.controller;

import com.oyproj.portal.service.PortalBrandService;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author oy
 * @description 首页品牌推荐管理Controller
 */
@RestController
@Tag(name = "PortalBrandController", description = "前台品牌管理")
@RequestMapping("/brand")
@RequiredArgsConstructor
public class PortalBrandController {
    private final PortalBrandService homeBrandService;
}
