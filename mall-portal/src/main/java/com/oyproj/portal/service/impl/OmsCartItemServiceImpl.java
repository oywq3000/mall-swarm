package com.oyproj.portal.service.impl;

import cn.hutool.core.collection.CollUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.CollectionUtils;
import com.oyproj.portal.dao.PortalProductDao;
import com.oyproj.portal.dto.CartProduct;
import com.oyproj.portal.dto.CartPromotionItem;
import com.oyproj.mall.mapper.OmsCartItemMapper;
import com.oyproj.mall.model.OmsCartItem;
import com.oyproj.mall.model.UmsMember;
import com.oyproj.portal.service.OmsCartItemService;
import com.oyproj.portal.service.OmsPromotionService;
import com.oyproj.portal.service.UmsMemberService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
@Service
@RequiredArgsConstructor
public class OmsCartItemServiceImpl implements OmsCartItemService {

    private final OmsCartItemMapper cartItemMapper;

    private final PortalProductDao productDao;

    private final OmsPromotionService promotionService;

    private final UmsMemberService memberService;
    /**
     * 查询购物车中是否包含该商品，有增加数量，无添加到购物车
     *
     * @param cartItem
     */
    @Override
    public int add(OmsCartItem cartItem) {
        int count;
        UmsMember currentMember = memberService.getCurrentMember();
        cartItem.setMemberId(currentMember.getId());
        cartItem.setMemberNickname(currentMember.getNickname());
        cartItem.setDeleteStatus(0);
        OmsCartItem existCartItem = getCartItem(cartItem);
        if(existCartItem==null){
            cartItem.setCreateDate(new Date());
            count = cartItemMapper.insert(cartItem);
        }else{
            cartItem.setModifyDate(new Date());
            existCartItem.setQuantity(existCartItem.getQuantity()+ cartItem.getQuantity());
            count = cartItemMapper.updateById(existCartItem);
        }
        return count;
    }

    /**
     * 根据会员编号获取购物车列表
     *
     * @param memberId
     */
    @Override
    public List<OmsCartItem> list(Long memberId) {
        LambdaQueryWrapper<OmsCartItem> lambdaQueryWrapper = new LambdaQueryWrapper<>();
        lambdaQueryWrapper.eq(OmsCartItem::getDeleteStatus,0).eq(OmsCartItem::getMemberId,memberId);
        return cartItemMapper.selectList(lambdaQueryWrapper);

    }

    /**
     * 获取包含促销活动信息的购物车列表
     * @param memberId
     * @param cartIds
     */
    @Override
    public List<CartPromotionItem> listPromotion(Long memberId, List<Long> cartIds) {

        List<OmsCartItem> cartItemList = list(memberId);
        //如果指定了购物车id，则只返回指定购物车中的商品
        if(CollUtil.isNotEmpty(cartIds)){
            cartItemList = cartItemList.stream().filter(item->cartIds.contains(item.getId())).toList();
        }
        List<CartPromotionItem> cartPromotionItemList = new ArrayList<>();
        if(CollectionUtils.isNotEmpty(cartItemList)){
            cartPromotionItemList = promotionService.calcCartPromotion(cartItemList);
        }
        return cartPromotionItemList;
    }

    @Override
    public List<CartPromotionItem> listPromotion(OmsCartItem cartItem) {

        List<OmsCartItem> cartItemList = List.of(cartItem);
        //如果指定了购物车id，则只返回指定购物车中的商品

        List<CartPromotionItem> cartPromotionItemList = new ArrayList<>();
        if(CollectionUtils.isNotEmpty(cartItemList)){
            cartPromotionItemList = promotionService.calcCartPromotion(cartItemList);
        }
        return cartPromotionItemList;
    }

    /**
     * 修改某个购物车商品的数量
     *
     * @param id
     * @param memberId
     * @param quantity
     */
    @Override
    public int updateQuantity(Long id, Long memberId, Integer quantity) {

        OmsCartItem cartItem = new OmsCartItem();
        cartItem.setQuantity(quantity);
        LambdaQueryWrapper<OmsCartItem> lambdaQueryWrapper = new LambdaQueryWrapper<>();
        lambdaQueryWrapper.eq(OmsCartItem::getDeleteStatus,0)
                .eq(OmsCartItem::getId,id)
                .eq(OmsCartItem::getMemberId,memberId);
        return cartItemMapper.update(cartItem,lambdaQueryWrapper);
    }

    /**
     * 批量删除购物车中的商品
     *
     * @param memberId
     * @param ids
     */
    @Override
    public int delete(Long memberId, List<Long> ids) {

        OmsCartItem record = new OmsCartItem();
        record.setDeleteStatus(1);
        LambdaQueryWrapper<OmsCartItem> lambdaQueryWrapper = new LambdaQueryWrapper<>();
        lambdaQueryWrapper.in(OmsCartItem::getId,ids).eq(OmsCartItem::getMemberId,memberId);
        return cartItemMapper.update(record,lambdaQueryWrapper);
    }

    /**
     * 获取购物车中用于选择商品规格的商品信息
     *
     * @param productId
     */
    @Override
    public CartProduct getCartProduct(Long productId) {

        return productDao.getCartProduct(productId);
    }

    /**
     * 修改购物车中商品的规格
     *
     * @param cartItem
     */
    @Override
    public int updateAttr(OmsCartItem cartItem) {
        OmsCartItem updateCart = new OmsCartItem();
        updateCart.setId(cartItem.getId());
        updateCart.setModifyDate(new Date());
        updateCart.setDeleteStatus(1);
        cartItemMapper.updateById(updateCart);
        cartItem.setId(null);
        add(cartItem);
        return 1;
    }

    /**
     * 清空购物车
     *
     * @param memberId
     */
    @Override
    public int clear(Long memberId) {

        OmsCartItem record = new OmsCartItem();
        record.setDeleteStatus(1);
        LambdaQueryWrapper<OmsCartItem> lambdaQueryWrapper = new LambdaQueryWrapper<>();
        lambdaQueryWrapper.eq(OmsCartItem::getMemberId,memberId);
        return cartItemMapper.update(record,lambdaQueryWrapper);
    }


    /**
     * 根据会员id,商品id和规格获取购物车中商品
     */
    private OmsCartItem getCartItem(OmsCartItem cartItem){
        LambdaQueryWrapper<OmsCartItem> lambdaQueryWrapper = new LambdaQueryWrapper<>();
        lambdaQueryWrapper.eq(OmsCartItem::getMemberId,cartItem.getMemberId())
                .eq(OmsCartItem::getProductId,cartItem.getProductId())
                .eq(OmsCartItem::getDeleteStatus,0);
        if(cartItem.getProductSkuId()!=null){
            lambdaQueryWrapper.eq(OmsCartItem::getProductSkuId,cartItem.getProductSkuId());
        }
        return cartItemMapper.selectOne(lambdaQueryWrapper);
    }
}
