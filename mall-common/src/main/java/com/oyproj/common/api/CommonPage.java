package com.oyproj.common.api;

import java.util.List;

/**
 * @author oy
 * @description 分页数据封装类
 */
import lombok.Data;
import org.springframework.data.domain.Page;

@Data
public class CommonPage<T> {
    private Integer pageNum;
    private Integer pageSize;
    private Integer totalPage;
    private Long total;
    private List<T> list;

    /**
     * 将PageHelper分页后的list转为分页信息
     */
    public static <T> CommonPage<T> restPage(IPageInfo<T> page) {
        CommonPage<T> result = new CommonPage<T>();
        result.setTotalPage((int)page.getPages());//page.getTotal()指代总共
        result.setPageNum((int)page.getCurrent());
        result.setPageSize((int)page.getSize()); //page.getSize()指代
        result.setTotal(page.getTotal());
        result.setList(page.getRecords());
        return result;
    }

    public static <T> CommonPage<T> restPage(Page<T> pageInfo) {
        CommonPage<T> result = new CommonPage<T>();
        result.setTotalPage(pageInfo.getTotalPages());
        result.setPageNum(pageInfo.getNumber());
        result.setPageSize(pageInfo.getSize());
        result.setTotal(pageInfo.getTotalElements());
        result.setList(pageInfo.getContent());
        return result;
    }

}
