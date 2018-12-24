package com.cr.mmall.controller.portal;


import com.cr.mmall.common.ServerResponse;
import com.cr.mmall.service.IProductService;
import com.cr.mmall.vo.ProductDetailVo;
import com.github.pagehelper.PageInfo;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.annotation.Resource;

@Controller
@RequestMapping("/product")
public class ProductController {
    @Resource
    private IProductService iProductService;

    // 获取产品详情
    @RequestMapping("/detail")
    @ResponseBody
    public ServerResponse<ProductDetailVo> detail(Integer productId) {
        return iProductService.getProductDetail(productId);
    }

    // 产品搜索及动态排序
    @RequestMapping("/list")
    @ResponseBody
    public ServerResponse<PageInfo> list(@RequestParam(value = "keyword", required = false) String keyword,
                                         @RequestParam(value = "categoryId", required = false) Integer categoryId,
                                         @RequestParam(value = "pageNum", defaultValue = "1") int pageNum,
                                         @RequestParam(value = "pageSize", defaultValue = "10") int pageSize,
                                         @RequestParam(value = "orderBy", defaultValue = "") String orderBy) {
        return iProductService.getProductByKeyWordCategory(keyword, categoryId, pageNum, pageSize, orderBy);
    }
}
