package com.baidu.shop.service;

import com.baidu.shop.base.Result;
import com.baidu.shop.dto.SpecGroupDTO;
import com.baidu.shop.entity.SpecGroupEntity;
import com.baidu.shop.validate.group.MingruiOperation;
import com.google.gson.JsonObject;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Api(tags = "规格接口")
public interface SpecGourpServiceI {

    @ApiOperation(value = "通过条件查询规格组")
    @GetMapping(value="/specGroup/getSepcGroupInfo")
    Result<List<SpecGroupEntity>>  getSepcGroupInfo(SpecGroupDTO specGroupDTO);

    @ApiOperation(value = "新增规格组")
    @PostMapping(value="/specGroup/saveSepcGroupInfo")
    Result<JsonObject>  saveSepcGroupInfo(@Validated({MingruiOperation.add.class}) @RequestBody SpecGroupDTO specGroupDTO);

    @ApiOperation(value = "修改规格组")
    @PutMapping(value="/specGroup/saveSepcGroupInfo")
    Result<JsonObject>  editSepcGroupInfo(@Validated({MingruiOperation.update.class}) @RequestBody SpecGroupDTO specGroupDTO);

    @ApiOperation(value = "删除规格组")
    @DeleteMapping(value="/specGroup/deleteSepcGroupInfo")
    Result<JsonObject>  deleteSepcGroupInfo(Integer id);
}
