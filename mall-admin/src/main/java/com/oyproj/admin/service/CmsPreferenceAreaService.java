package com.oyproj.admin.service;


import com.oyproj.mall.model.CmsPrefrenceArea;

import java.util.List;

public interface CmsPreferenceAreaService {
    /**
     * 获取所有优选专区
     */
    List<CmsPrefrenceArea> listAll();
}
