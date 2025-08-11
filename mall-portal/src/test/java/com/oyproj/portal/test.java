package com.oyproj.portal;

import com.oyproj.mall.model.UmsMember;
import com.oyproj.portal.dto.UmsUpdateMemberInfoDto;
import org.junit.jupiter.api.Test;
import org.springframework.beans.BeanUtils;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
public class test {


    @Test
    public void TestBeancopy(){
        UmsMember umsMember = new UmsMember();
        umsMember.setId(1l);
        umsMember.setNickname("欧阳1");
        umsMember.setPhone("19808018616");
        System.out.println(umsMember.getPhone());
        UmsUpdateMemberInfoDto umsUpdateMemberInfoDto = new UmsUpdateMemberInfoDto();
        umsUpdateMemberInfoDto.setGender(1);
        BeanUtils.copyProperties(umsUpdateMemberInfoDto,umsMember,getNullPropertyNames(umsUpdateMemberInfoDto));
        System.out.println(umsMember);
    }

    private String[] getNullPropertyNames(Object source) {
        org.springframework.beans.BeanWrapper src = new org.springframework.beans.BeanWrapperImpl(source);
        java.beans.PropertyDescriptor[] pds = src.getPropertyDescriptors();

        java.util.Set<String> emptyNames = new java.util.HashSet<>();
        for (java.beans.PropertyDescriptor pd : pds) {
            Object srcValue = src.getPropertyValue(pd.getName());
            if (srcValue == null) emptyNames.add(pd.getName());
        }
        String[] result = new String[emptyNames.size()];
        return emptyNames.toArray(result);
    }

}