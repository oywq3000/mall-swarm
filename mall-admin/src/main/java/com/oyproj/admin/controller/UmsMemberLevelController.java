package com.oyproj.admin.controller;

import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author oy
 * @description 会员等价管理Controller
 */
@RestController
@Tag(name = "UmsMemberLevelController", description = "会员等级管理")
@RequestMapping("/memberLevel")
public class UmsMemberLevelController {
}
