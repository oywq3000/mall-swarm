package com.oyproj.portal.component;

import com.oyproj.portal.service.OmsPortalOrderService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.annotation.RabbitHandler;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

/**
 * @author oy
 * @description 取消订单消息的处理者
 */
@Component
@Slf4j
@RabbitListener(queues = "mall.order.cancel")
@RequiredArgsConstructor
public class CancelOrderReceiver {
    private final OmsPortalOrderService portalOrderService;
    @RabbitHandler
    public void handle(Long orderId){
        portalOrderService.cancelOrder(orderId);
        log.info("process orderId:{}",orderId);
    }

}
