package com.oyproj.common.api;


import java.util.List;

/**
 * @author oy
 * @description 提供pageinfo接口，满足不同模块
 */
public interface IPageInfo<T> {
     long getTotal();
     int getPages();
     long getCurrent();
     int getSize();
     List<T> getRecords();
}
