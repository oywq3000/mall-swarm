package com.oyproj.admin;

import com.oyproj.admin.dto.SmsFlashPromotionProduct;
import com.oyproj.admin.service.SmsFlashPromotionProductRelationService;
import com.oyproj.common.api.IPageInfo;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import reactor.core.CoreSubscriber;
import reactor.core.publisher.Mono;

@SpringBootTest
public class SmsFlashPromotionProductRelationServiceTest {
    @Autowired
    private SmsFlashPromotionProductRelationService service;
    @Test
    void TestList(){
        IPageInfo<SmsFlashPromotionProduct> list = service.list(2L,1L,1,5);
        System.out.println(list.getRecords());

    }
    @Test
    void TestReactor(){
        Mono<String> mono = Mono.just("hello world");

        mono.subscribe(System.out::println);

    }
}
