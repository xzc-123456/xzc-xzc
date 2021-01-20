package com.baidu.shop.service.impl;

import com.baidu.shop.base.BaseApiService;
import com.baidu.shop.base.Result;
import com.baidu.shop.entity.CategoryEntity;
import com.baidu.shop.mapper.CategoryMapper;
import com.baidu.shop.service.CategoryService;
import com.google.gson.JsonObject;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import java.util.List;

/**
 * @ClassName CategoryServiceImpl
 * @Description: TODO
 * @Author xuezhaochang
 * @Date 2021/1/19
 * @Version V1.0
 **/
@RestController
public class CategoryServiceImpl extends BaseApiService implements CategoryService {
    @Resource
    private CategoryMapper categoryMapper;

    @Override
    public Result<List<CategoryEntity>> getCategoryById(Integer pid) {
        CategoryEntity categoryEntity = new CategoryEntity();
        categoryEntity.setParentId(pid);
        List<CategoryEntity> list = categoryMapper.select(categoryEntity);
        return this.setResultSuccess(list);
    }

    @Override
    public Result<JsonObject> saveCategory(CategoryEntity categoryEntity) {
        CategoryEntity parentCategory = new CategoryEntity();
        parentCategory.setId(categoryEntity.getParentId());
        parentCategory.setIsParent(1);
        categoryMapper.updateByPrimaryKeySelective(parentCategory);

        categoryMapper.insertSelective(categoryEntity);
        return this.setResultSuccess();
    }
}
