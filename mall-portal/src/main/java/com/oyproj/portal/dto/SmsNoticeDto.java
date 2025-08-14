package com.oyproj.portal.dto;

import com.oyproj.mall.model.SmsNotice;
import lombok.Data;

import java.util.Date;

@Data
public class SmsNoticeDto extends SmsNotice {
    private Date sendDate;
}
