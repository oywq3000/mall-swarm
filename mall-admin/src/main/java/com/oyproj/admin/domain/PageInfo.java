package com.oyproj.admin.domain;

import com.oyproj.common.api.CommonResult;
import com.oyproj.common.api.IPageInfo;
import lombok.Data;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import java.util.List;
/**
 * @author oy
 * @description 自定义模块page，使用mybatis扩展来一键为
 */

@Data
public class PageInfo<T> implements IPageInfo<T> {
    private long total;
    private int pages;
    private long current;
    private int size;
    private List<T> records;

    PageInfo(long total,int pages,long current,int size,List<T> records){
        this.total = total;
        this.pages = pages;
        this.current = current;
        this.size = size;
        this.records = records;
    }

    /**
     * 从mybatisplus
     */
    public static <T> PageInfo<T> build(Page<T> page){
        return new PageInfo<>(page.getTotal(),(int) page.getPages(),page.getCurrent(),(int) page.getSize(),page.getRecords());
    }
}
