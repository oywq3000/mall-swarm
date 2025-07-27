package com.oyproj.search.dao;


import com.oyproj.search.dto.EsProduct;
import org.apache.ibatis.annotations.Param;


import java.util.List;

/**
 * @author oy
 * @description 搜索商品管理自定义Dao
 */
public interface EsProductDao {
    /**
     * 获取指定ID的搜索商品
     */
    List<EsProduct> getAllEsProductList(@Param("id") Long id);
}
