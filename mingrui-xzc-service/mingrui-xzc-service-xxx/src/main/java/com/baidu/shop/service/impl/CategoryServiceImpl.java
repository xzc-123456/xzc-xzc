package com.baidu.shop.service.impl;

import com.baidu.shop.base.BaseApiService;
import com.baidu.shop.base.Result;
import com.baidu.shop.entity.CategoryEntity;
import com.baidu.shop.mapper.CategoryMapper;
import com.baidu.shop.service.CategoryService;
import com.google.gson.JsonObject;
import org.apache.commons.lang.ObjectUtils;
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

    @Override
    public Result<List<CategoryEntity>> getCategoryByBrandId(Integer brandId) {
        return null;
    }

    @Override
    public Result<JsonObject> deleteById(Integer id) {
        //校验id是否合法
        if (ObjectUtils.isNull(id) || id <= 0 ) return this.setResultError("id不合法");

        //通过id查询一条节点数据
        CategoryEntity categoryEntity = categoryMapper.selectByPrimaryKey(id);

        //判断查询出来的数据是否存在。如果查询出来的结果返回null/证明数据不存在
        if (ObjectUtils.isNull(categoryEntity)) return this.setResultError("数据不存在");

        //如果查询出来的数据的isparent字段状态为1.那么说明时父节点。就不能删除。
        if (categoryEntity.getIsParent() == 1) return this.setResultError("当前节点为父节点。不能删除");

        //如果当前分类被品牌绑定的话不能被删除 --> 通过分类id查询中间表是否有数据 true : 当前分类不能被删除 false : 继续执行
        Example example1 = new Example(CategoryBrandEntity.class);
        example1.createCriteria().andEqualTo("categoryId",id);
        List<CategoryBrandEntity> categoryBrandList = categoryBrandMapper.selectByExample(example1);
        if(categoryBrandList.size() != 0) return this.setResultError("此数据绑定有品牌信息,不能被删除哟");



        //相当于把实体类放入这个类中，这个类中的方法可以操作SQL语句。他是tkMapper中的类
        Example example = new Example(CategoryEntity.class);
        //对sql进行拼接 select * from 表名 where 1=1 and parentId = ?
        example.createCriteria().andEqualTo("parentId",categoryEntity.getParentId());
        //将查出的数据装入list集合中
        List<CategoryEntity> list = categoryMapper.selectByExample(example);

        //通过id删除节点
        if (list.size() <= 1){
            CategoryEntity updateCategory = new CategoryEntity();
            updateCategory.setIsParent(0);
            updateCategory.setId(categoryEntity.getParentId());
            categoryMapper.updateByPrimaryKeySelective(updateCategory);
        }
        categoryMapper.deleteByPrimaryKey(id);
        return this.setResultSuccess();
    }

    @Override
    public Result<JsonObject> editCategory(CategoryEntity CategoryEntity) {
        return null;
    }
}
