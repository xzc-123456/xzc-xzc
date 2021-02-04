package com.baidu.shop.service;

import com.baidu.shop.base.Result;
import com.baidu.shop.dto.BrandDTO;
import com.baidu.shop.entity.BrandEntity;
import com.baidu.shop.validate.group.MingruiOperation;
import com.github.pagehelper.PageInfo;
import com.google.gson.JsonObject;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * @ClassName BrandService
 * @Description: TODO
 * @Author xuezhaochang
 * @Date 2020/12/25
 * @Version V1.0
 **/
@Api(tags = "品牌接口")
public interface BrandService {
    @ApiOperation(value = "获取品牌信息")
    @GetMapping(value = "/brand/list")
    Result<PageInfo<BrandEntity>> getBrandInfo(BrandDTO brandDTO);

    @ApiOperation(value = "新增品牌信息")
    @PostMapping(value = "/brand/save")
    Result<JsonObject> saveBrandInfo(@Validated({MingruiOperation.add.class}) @RequestBody BrandDTO brandDTO);

    @ApiOperation(value = "修改品牌列表")
    @PutMapping(value = "/brand/save")
    Result<JsonObject> editBrandInfo(@Validated({MingruiOperation.update.class}) @RequestBody BrandDTO brandDTO);

    @ApiOperation(value = "删除品牌列表")
    @DeleteMapping(value = "/brand/delete")
    Result<JsonObject> deleteBrandInfo(Integer id);

    @ApiOperation(value = "通过分类ID获得品牌")
    @GetMapping(value = "/brand/getBrandByCategory")
    Result<List<BrandEntity>> getBrandByCategory(Integer cid);

}
