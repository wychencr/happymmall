package com.cr.mmall.controller.portal;

import com.alipay.api.AlipayApiException;
import com.alipay.api.internal.util.AlipaySignature;
import com.alipay.demo.trade.config.Configs;
import com.cr.mmall.common.Const;
import com.cr.mmall.common.ResponseCode;
import com.cr.mmall.common.ServerResponse;
import com.cr.mmall.pojo.User;
import com.cr.mmall.service.IOrderService;
import com.google.common.collect.Maps;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.util.Map;

@Controller
@RequestMapping("/order")
public class OrderController {

    private static final Logger logger = LoggerFactory.getLogger(OrderController.class);

    @Resource
    private IOrderService iOrderService;


    // 创建订单
    @RequestMapping("/create")
    @ResponseBody
    public ServerResponse create(HttpSession session, Integer shippingId) {
        User user = (User) session.getAttribute(Const.CURRENT_USER);
        if (user == null) {
            return ServerResponse.createByErrorCodeMessage(ResponseCode.NEED_LOGIN.getCode(), ResponseCode.NEED_LOGIN.getDesc());
        }
        return iOrderService.create(user.getId(), shippingId);
    }

    // 取消订单
    @RequestMapping("/cancel")
    @ResponseBody
    public ServerResponse cancel(HttpSession session, Long orderNo) {
        User user = (User) session.getAttribute(Const.CURRENT_USER);
        if (user == null) {
            return ServerResponse.createByErrorCodeMessage(ResponseCode.NEED_LOGIN.getCode(), ResponseCode.NEED_LOGIN.getDesc());
        }
        return iOrderService.cancel(user.getId(), orderNo);
    }

    // 获取订单的商品信息
    @RequestMapping("/get_order_cart_product")
    @ResponseBody
    public ServerResponse getOrderCartProduct(HttpSession session) {
        User user = (User) session.getAttribute(Const.CURRENT_USER);
        if (user == null) {
            return ServerResponse.createByErrorCodeMessage(ResponseCode.NEED_LOGIN.getCode(), ResponseCode.NEED_LOGIN.getDesc());
        }
        return iOrderService.getOrderCartProduct(user.getId());
    }

    // 获取订单详情
    @RequestMapping("/detail")
    @ResponseBody
    public ServerResponse detail(HttpSession session, Long orderNo) {
        User user = (User) session.getAttribute(Const.CURRENT_USER);
        if (user == null) {
            return ServerResponse.createByErrorCodeMessage(ResponseCode.NEED_LOGIN.getCode(), ResponseCode.NEED_LOGIN.getDesc());
        }
        return iOrderService.detail(user.getId(), orderNo);
    }

    // 获取用户订单分页列表
    @RequestMapping("/list")
    @ResponseBody
    public ServerResponse list(@RequestParam(value = "pageNum", defaultValue = "1") int pageNum,
                               @RequestParam(value = "pageSize", defaultValue = "10") int pageSize,
                               HttpSession session) {
        User user = (User) session.getAttribute(Const.CURRENT_USER);
        if (user == null) {
            return ServerResponse.createByErrorCodeMessage(ResponseCode.NEED_LOGIN.getCode(), ResponseCode.NEED_LOGIN.getDesc());
        }
        return iOrderService.getOrderList(user.getId(), pageNum, pageSize);
    }


    /** 支付模块*/

    // 订单支付(根据订单号下单，并生成支付宝的二维码url返回)
    @RequestMapping("/pay")
    @ResponseBody
    public ServerResponse pay(HttpSession session, Long orderNo, HttpServletRequest request) {
        User user = (User) session.getAttribute(Const.CURRENT_USER);
        if (user == null) {
            return ServerResponse.createByErrorCodeMessage(ResponseCode.NEED_LOGIN.getCode(), ResponseCode.NEED_LOGIN.getDesc());
        }
        // 获取二维码上传路径
        String path = request.getSession().getServletContext().getRealPath("upload");
        return iOrderService.pay(user.getId(), path, orderNo);
    }

    // 支付宝回调
    @RequestMapping("/alipay_callback")
    @ResponseBody
    public Object alipayCallback(HttpServletRequest request) {
        // Map<String, String[]> --> Map<String, String> 作为后面的验签参数
        Map<String, String> params = Maps.newHashMap();
        Map requestParams = request.getParameterMap();
        for (Object key : requestParams.keySet()) {
            String[] values = (String[]) requestParams.get(key);
            String valueStr = "";
            for (int i = 0; i < values.length; ++i) {
                valueStr = (i == values.length - 1) ? valueStr + values[i] : valueStr + values[i] + ",";
            }
            params.put(key.toString(), valueStr);
        }
        logger.info("支付宝回调, sign:{}, trade_status:{}, 参数:{}", params.get("sign"), params.get("trade_status"), params.toString());

        /** [非常重要]验证回调的正确性，是否来自于支付宝，同时还要避免重复通知 */

        // 除去sign(sdk中已经移除)、sign_type两个参数，对其他参数进行验签
        params.remove("sign_type");
        try {
            boolean alipayRSACheckedV2 = AlipaySignature.rsaCheckV2(params, Configs.getAlipayPublicKey(), "utf-8", Configs.getSignType());
            if (!alipayRSACheckedV2) {
                return ServerResponse.createByErrorMessage("非法请求，验证不通过!!!");
            }
        } catch (AlipayApiException e) {
            logger.info("支付宝回调异常", e);
        }

        // 验证各种数据并返回给支付宝服务器通知
        ServerResponse serverResponse = iOrderService.aliCallback(params);
        if(serverResponse.isSuccess()) {
            return Const.AlipayCallback.RESPONSE_SUCCESS;
        }
        return Const.AlipayCallback.RESPONSE_FAILED;
    }

    // 查询订单支付状态
    @RequestMapping("/query_order_pay_status")
    @ResponseBody
    public ServerResponse<Boolean> queryOrderPayStatus(HttpSession session, Long orderNo) {
        User user = (User) session.getAttribute(Const.CURRENT_USER);
        if (user == null) {
            return ServerResponse.createByErrorCodeMessage(ResponseCode.NEED_LOGIN.getCode(), ResponseCode.NEED_LOGIN.getDesc());
        }

        ServerResponse serverResponse = iOrderService.queryOrderPayStatus(user.getId(), orderNo);
        if (serverResponse.isSuccess()) {
            return ServerResponse.createBySuccess(true);
        }
        return ServerResponse.createBySuccess(false);
    }
}
