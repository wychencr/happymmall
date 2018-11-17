package com.cr.mmall.service;

import com.cr.mmall.common.ServerResponse;
import com.cr.mmall.pojo.Product;
import com.cr.mmall.vo.ProductDetailVo;
import com.github.pagehelper.PageInfo;

public interface IProductService {
    ServerResponse saveOrUpdateProduct(Product product);

    ServerResponse<String> setSaleStatus(Integer productId, Integer status);

    ServerResponse<ProductDetailVo> manageProductDetail(Integer productId);

    ServerResponse<PageInfo> getProductList(int pageNum, int pageSize);

    ServerResponse<PageInfo> searchProduct(String productName, Integer productId, int pageNum, int pageSize);

    ServerResponse<ProductDetailVo> getProductDetail(Integer productId);

    ServerResponse<PageInfo> getProductByKeyWordCategory(String keyword, Integer categoryId, int pageNum, int pageSize, String orderBy);

    }
