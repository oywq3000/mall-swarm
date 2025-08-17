package com.oyproj.portal.dto;

import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper = false)
public class OrderByProductParam extends OrderParam{
    private ProductDto productDto;
}
