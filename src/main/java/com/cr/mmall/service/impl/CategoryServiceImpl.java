package com.cr.mmall.service.impl;

import com.cr.mmall.common.ServerResponse;
import com.cr.mmall.mapper.CategoryMapper;
import com.cr.mmall.pojo.Category;
import com.cr.mmall.service.ICategoryService;
import com.google.common.collect.Lists;
import com.google.common.collect.Sets;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;
import java.util.Set;

@Service("iCategoryService")
public class CategoryServiceImpl implements ICategoryService {

    private Logger logger = LoggerFactory.getLogger(CategoryServiceImpl.class);

    @Resource
    private CategoryMapper categoryMapper;

    @Override
    public ServerResponse addCategory(String categoryName, Integer parentId) {
        if ((parentId == null) || (StringUtils.isBlank(categoryName))) {
            return ServerResponse.createByErrorMessage("添加品类参数错误");
        }

        Category category = new Category();
        category.setName(categoryName);
        category.setParentId(parentId);
        category.setStatus(true);

        int rowCount = categoryMapper.insert(category);
        if (rowCount > 0) {
            return ServerResponse.createBySuccessMessage("添加品类成功");
        }
        return ServerResponse.createByErrorMessage("添加品类失败");
    }

    @Override
    public ServerResponse updateCategoryName(Integer categoryId, String categoryName) {
        if ((categoryId == null) || (StringUtils.isBlank(categoryName))) {
            return ServerResponse.createByErrorMessage("更新品类参数错误");
        }

        Category category = new Category();
        category.setId(categoryId);
        category.setName(categoryName);

        int rowCount = categoryMapper.updateByPrimaryKeySelective(category);
        if (rowCount > 0) {
            return ServerResponse.createBySuccess("更新品类名称成功");
        }
        return ServerResponse.createByErrorMessage("更新品类名称失败");
    }

    @Override
    public ServerResponse<List<Category>> getChildrenParallelCategory(Integer categoryId) {
        if (categoryId == null) {
            return ServerResponse.createByErrorMessage("查询品类参数错误");
        }

        List<Category> categoryList = categoryMapper.selectCategoryChildrenByParentId(categoryId);
        if (CollectionUtils.isEmpty(categoryList)) {
            logger.info("未找到当前分类的子分类");
        }
        return ServerResponse.createBySuccess(categoryList);
    }

    @Override
    public ServerResponse<List<Integer>> selectCategoryAndChildrenById(Integer categoryId) {
        Set<Category> categorySet = Sets.newHashSet();
        findChildCategory(categorySet, categoryId);

        // 将set转为list返回
        List<Integer> categoryIdList = Lists.newArrayList();
        if (categoryId != null) {
            for (Category categoryItem : categorySet) {
                categoryIdList.add(categoryItem.getId());
            }
        }
        return ServerResponse.createBySuccess(categoryIdList);
    }

    // 递归算法，算出所有的子节点
    private Set<Category> findChildCategory(Set<Category> categorySet, Integer categoryId) {
        // 查找当前节点，加入到Set中
        Category category = categoryMapper.selectByPrimaryKey(categoryId);
        if (category != null) {
            categorySet.add(category);
        }
        // 查找次级所有子节点
        List<Category> categoryList = categoryMapper.selectCategoryChildrenByParentId(categoryId);
        // 如果查不到子节点，则不会执行以下循环；如果查到了，则遍历子节点，递归
        for (Category categoryItem : categoryList) {
            findChildCategory(categorySet, categoryItem.getId());
        }
        return categorySet;
    }
}
