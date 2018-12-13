package com.cr.mmall.service;

import com.cr.mmall.common.ServerResponse;
import com.cr.mmall.vo.OrderProductVo;
import com.cr.mmall.vo.OrderVo;
import com.github.pagehelper.PageInfo;

import java.util.Map;

public interface IOrderService {
    ServerResponse pay(Integer userId, String path, Long orderNo);

    ServerResponse aliCallback(Map<String, String> params);

    ServerResponse queryOrderPayStatus(Integer userId, Long orderNo);

    ServerResponse<OrderVo> create(Integer userId, Integer shippingId);

    ServerResponse<String> cancel(Integer userId, Long orderNo);

    ServerResponse<OrderProductVo> getOrderCartProduct(Integer userId);

    ServerResponse<OrderVo> detail(Integer userId, Long orderNo);

    ServerResponse<PageInfo> getOrderList(Integer userId, int pageNum, int pageSize);

    ServerResponse<PageInfo> manageList(int pageNum, int pageSize);

    ServerResponse<OrderVo> manageDetail(Long orderNo);

    ServerResponse<PageInfo> manageSearch(Long orderNo, int pageNum, int pageSize);

    ServerResponse<String> manageSendGoods(Long orderNo);
}
