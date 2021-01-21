package com.baidu.shop.service;

import com.baidu.shop.base.Result;
import com.baidu.shop.entity.CategoryEntity;
import com.google.gson.JsonObject;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Api(tags = "商品分类接口")
public interface CategoryService {
    @ApiOperation(value = "通过父Id查询商品分类")
    @GetMapping(value = "category/list")
    Result<List<CategoryEntity>> getCategoryById(Integer pid);

    @ApiOperation(value = "新增商品分类")
    @PostMapping(value = "category/save")
    Result<JsonObject> saveCategory(CategoryEntity categoryEntity);

    @ApiOperation(value = "通过品牌ID查询分类信息")
    @GetMapping(value = "category/brand")
    Result<List<CategoryEntity>> getCategoryByBrandId(Integer brandId);

    @ApiOperation(value = "通过id删除分类")
    @DeleteMapping(value = "/category/del")
    Result<JsonObject> deleteById(Integer id);

    @ApiOperation(value = "修改分类名称")
    @PutMapping(value = "/category/edit")
    Result<JsonObject> editCategory(@RequestBody CategoryEntity CategoryEntity);
}
