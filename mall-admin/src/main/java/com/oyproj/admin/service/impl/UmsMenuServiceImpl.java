package com.oyproj.admin.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.oyproj.admin.domain.PageInfo;
import com.oyproj.admin.dto.UmsMenuNode;
import com.oyproj.mall.mapper.UmsMenuMapper;
import com.oyproj.mall.model.UmsMenu;
import com.oyproj.admin.service.UmsMenuService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class UmsMenuServiceImpl implements UmsMenuService {

    private final UmsMenuMapper menuMapper;
    /**
     * 创建后台菜单
     *
     * @param umsMenu
     */
    @Override
    public int create(UmsMenu umsMenu) {
        umsMenu.setCreateTime(new Date());
        updateLevel(umsMenu);
        return menuMapper.insert(umsMenu);
    }

    /**
     * 修改后台菜单
     *
     * @param id
     * @param umsMenu
     */
    @Override
    public int update(Long id, UmsMenu umsMenu) {
        umsMenu.setId(id);
        updateLevel(umsMenu);
        return menuMapper.updateById(umsMenu);
    }

    /**
     * 根据ID获取菜单详情
     *
     * @param id
     */
    @Override
    public UmsMenu getItem(Long id) {
        return menuMapper.selectById(id);
    }

    /**
     * 根据ID删除菜单
     *
     * @param id
     */
    @Override
    public int delete(Long id) {
        return menuMapper.deleteById(id);
    }

    /**
     * 分页查询后台菜单
     *
     * @param parentId
     * @param pageSize
     * @param pageNum
     */
    @Override
    public PageInfo<UmsMenu> list(Long parentId, Integer pageSize, Integer pageNum) {
        Page<UmsMenu> page1 = new Page<>(pageNum,pageSize);
        LambdaQueryWrapper<UmsMenu> lambdaQueryWrapper = new LambdaQueryWrapper<>();
        lambdaQueryWrapper.eq(UmsMenu::getParentId,parentId).orderByDesc(UmsMenu::getSort);
        Page<UmsMenu> umsMenuPage = menuMapper.selectPage(page1, lambdaQueryWrapper);
        return PageInfo.build(umsMenuPage);
    }

    /**
     * 树形结构返回所有菜单列表
     */
    @Override
    public List<UmsMenuNode> treeList() {
        List<UmsMenu> umsMenus = menuMapper.selectList(new QueryWrapper<>());
        List<UmsMenuNode> result = umsMenus.stream()
                .filter(umsMenu -> umsMenu.getParentId().equals(0L))
                .map(umsMenu -> convertMenuNode(umsMenu, umsMenus))
                .collect(Collectors.toList());
        return result;
    }


    /**
     * 修改菜单显示状态
     *
     * @param id
     * @param hidden
     */
    @Override
    public int updateHidden(Long id, Integer hidden) {
        UmsMenu umsMenu = new UmsMenu();
        umsMenu.setId(id);
        umsMenu.setHidden(hidden);
        return menuMapper.updateById(umsMenu);
    }



    /**
     * 修改菜单层级
     */
    private void updateLevel(UmsMenu umsMenu) {
        if (umsMenu.getParentId() == 0) {
            //没有父菜单时为一级菜单
            umsMenu.setLevel(0);
        } else {
            //有父菜单时选择根据父菜单level设置
            UmsMenu parentMenu = menuMapper.selectById(umsMenu.getParentId());
            if (parentMenu != null) {
                umsMenu.setLevel(parentMenu.getLevel() + 1);
            } else {
                umsMenu.setLevel(0);
            }
        }
    }
    private UmsMenuNode convertMenuNode(UmsMenu umsMenu, List<UmsMenu> umsMenus)  {
        UmsMenuNode node = new UmsMenuNode();
        BeanUtils.copyProperties(umsMenu,node);
        List<UmsMenuNode> children = umsMenus.stream()
                .filter(subMenu -> subMenu.getParentId().equals(umsMenu.getId()))
                .map(subMenu -> convertMenuNode(subMenu, umsMenus)).collect(Collectors.toList());
        node.setChildren(children);
        return node;
    }

}
