package com.oyproj.portal.component;


import com.oyproj.portal.service.OmsPortalOrderService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;

/**
 * @author oy
 * @description 订单超时取消并解锁库存的定时器
 */
//暂时未启用
@Slf4j
@RequiredArgsConstructor
public class OrderTimeOutCancelTask {
    private final OmsPortalOrderService portalOrderService;
    /**
     * cron表达式：Seconds Minutes Hours DayofMonth Month DayofWeek [Year]
     * 每10分钟扫描一次，扫描设定超时时间之前下的订单，如果没支付则取消该订单
     */
    @Scheduled(cron = "0 0/10 * ? * ?")
    private void cancelTimeOutOrder(){
        Integer count = portalOrderService.cancelTimeOutOrder();
        log.info("取消订单，并根据sku编号释放锁定库存，取消订单数量：{}",count);
    }
}
