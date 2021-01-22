package com.baidu.shop.service.impl;

import com.baidu.shop.base.BaseApiService;
import com.baidu.shop.base.Result;
import com.baidu.shop.entity.CategoryEntity;
import com.baidu.shop.mapper.CategoryMapper;
import com.baidu.shop.service.CategoryService;
import com.baidu.shop.utils.ObjectUtils;
import com.google.gson.JsonObject;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.RestController;
import tk.mybatis.mapper.entity.Example;

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
    @Transactional
    @Override
    public Result<JsonObject> deleteCategoryById(Integer id) {
        //首先判断id是否合法
        if (ObjectUtils.isNull(id) || id <= 0) return this.setResultError("id不合法");
        CategoryEntity categoryEntity = categoryMapper.selectByPrimaryKey(id);
        if (ObjectUtils.isNull(categoryEntity)) return this.setResultError("数据不存在");
        //判断当前节点是否为父节点
        if (categoryEntity.getIsParent() == 1) return this.setResultError("当前节点为父节点,不能被删除");
        //通过当前节点的父节点id查询当前节点d的父节点下是否还有其他节点
        //相当于把实体类放入这个类中，这个类中的方法可以操作SQL语句。他是tkMapper中的类
        Example example = new Example(CategoryEntity.class);
        //对sql进行拼接 select * from 表名 where 1=1 and parentId = ?
        example.createCriteria().andEqualTo("parentId",categoryEntity.getParentId());
        //将查出的数据装入list集合中
        List<CategoryEntity> list = categoryMapper.selectByExample(example);
        if (list.size() <= 1){
            CategoryEntity updateCategoryEntity = new CategoryEntity();
            updateCategoryEntity.setId(categoryEntity.getParentId());
            updateCategoryEntity.setIsParent(0);

            categoryMapper.updateByPrimaryKeySelective(updateCategoryEntity);
        }

        categoryMapper.deleteByPrimaryKey(id);
        return this.setResultSuccess();
    }

}
