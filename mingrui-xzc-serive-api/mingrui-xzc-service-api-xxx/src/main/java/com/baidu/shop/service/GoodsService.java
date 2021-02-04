package com.baidu.shop.service;

import com.baidu.shop.base.Result;
import com.baidu.shop.dto.SkuDTO;
import com.baidu.shop.dto.SpuDTO;
import com.baidu.shop.entity.SpuDetailEntity;
import com.baidu.shop.entity.SpuEntity;
import com.baidu.shop.validate.group.MingruiOperation;
import com.google.gson.JsonObject;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Api(tags = "商品接口")
public interface GoodsService {
    @ApiOperation(value = "获取spu信息")
    @GetMapping(value = "/goods/getSpuInfo")
    Result<List<SpuDTO>> getSpuInfo(SpuDTO spuDTO);

    @ApiOperation(value = "新增商品")
    @PostMapping(value = "/goods/save")
    Result<JsonObject> saveGoods(@Validated({MingruiOperation.add.class}) @RequestBody SpuDTO spuDTO);

    @ApiOperation(value = "通过spuId查询spuDetail商品信息")
    @GetMapping(value = "/goods/getSpudetailBySpuId")
    Result<SpuDetailEntity> getSpudetailBySpuId(Integer spuId);

    @ApiOperation(value = "通过spuId查询skus信息")
    @GetMapping(value = "/goods/getSkusBySpuId")
    Result<List<SkuDTO>> getSkusBySpuId(Integer spuId);

    @ApiOperation(value = "通过spuId修改skus信息")
    @PutMapping(value = "/goods/save")
    Result<JsonObject> editSkusBySpuId(@Validated({MingruiOperation.update.class}) @RequestBody SpuDTO spuDTO);

    @ApiOperation(value = "通过spuId删除商品信息")
    @DeleteMapping(value = "/goods/delete")
    Result<JsonObject> deleteGoods(Integer spuId);

    @ApiOperation(value = "上架或下架商品")
    @PutMapping(value = "/goods/shangjiaOrxiajia")
    Result<JsonObject> shangjiaOrxiajia(@Validated({MingruiOperation.update.class}) @RequestBody SpuDTO spuDTO);
}
