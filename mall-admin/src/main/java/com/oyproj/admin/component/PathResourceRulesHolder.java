package com.oyproj.admin.component;

import com.oyproj.admin.service.UmsResourceService;
import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

/**
 * @author oy
 * @description 路径与资源访问对应关系操作组件
 */
@Component
@RequiredArgsConstructor
public class PathResourceRulesHolder {
    private final UmsResourceService resourceService;
    @PostConstruct
    public void initPathResourceMap(){
        resourceService.initPathResourceMap();
    }
}
