package com.baidu.shop.service;

import com.baidu.shop.base.Result;
import com.baidu.shop.dto.SpecParamDTO;
import com.baidu.shop.entity.SpecParamEntity;
import com.baidu.shop.validate.group.MingruiOperation;
import com.google.gson.JsonObject;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Api(tags = "规格参数")
public interface SpecificationService {
    @ApiOperation(value = "查询规格参数")
    @GetMapping(value = "specparam/getSpecParamInfo")
    Result<List<SpecParamEntity>> getSpecParamInfo(SpecParamDTO specParamDTO);

    @ApiOperation(value = "新增规格参数")
    @PostMapping(value = "specparam/saveSpecParamInfo")
    Result<JsonObject> saveSpecParamInfo(@Validated({MingruiOperation.add.class}) @RequestBody SpecParamDTO specParamDTO);

    @ApiOperation(value = "新增规格参数")
    @PutMapping(value = "specparam/saveSpecParamInfo")
    Result<JsonObject> editSpecParamInfo(@Validated({MingruiOperation.update.class}) @RequestBody SpecParamDTO specParamDTO);

    @ApiOperation(value = "新增规格参数")
    @DeleteMapping(value = "specparam/delSpecParamInfo")
    Result<JsonObject> delSpecParamInfo(Integer id);
}
