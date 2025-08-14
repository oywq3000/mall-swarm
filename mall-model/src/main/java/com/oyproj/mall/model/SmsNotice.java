package com.oyproj.mall.model;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.io.Serializable;
import java.util.Date;

@Data
public class SmsNotice implements Serializable {
    private Long id;
    private String title;
    private String pic;
    @Schema(title = "0->活动结束，1->活动进行中")
    private Integer status;
    private String url;
    private String introduce;
    private Date startDate;
    private Date endDate;
}
