package com.oyproj.admin.service;


import com.baomidou.mybatisplus.extension.service.IService;
import com.oyproj.admin.domain.PageInfo;
import com.oyproj.admin.model.UmsMenu;
import com.oyproj.admin.model.UmsResource;
import com.oyproj.admin.model.UmsRole;


import java.util.List;
import java.util.Map;

/**
 * @author oy
 * @description 后台角色管理Service
 */
public interface UmsResourceService extends IService<UmsResource> {
    /**
     * 添加资源
     */
    int create(UmsResource umsResource);

    /**
     * 修改资源
     */
    int update(Long id, UmsResource umsResource);

    /**
     * 获取资源详情
     */
    UmsResource getItem(Long id);

    /**
     * 删除资源
     */
    int delete(Long id);

    /**
     * 分页查询资源
     */
    PageInfo<UmsResource> list(Long categoryId, String nameKeyword, String urlKeyword, Integer pageSize, Integer pageNum);

    /**
     * 查询全部资源
     */
    List<UmsResource> listAll();

    /**
     * 初始化路径与资源访问规则
     */
    Map<String,String> initPathResourceMap();
}
