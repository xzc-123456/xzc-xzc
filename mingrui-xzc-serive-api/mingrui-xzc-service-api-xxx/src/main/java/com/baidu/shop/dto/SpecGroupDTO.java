package com.baidu.shop.dto;

import com.baidu.shop.validate.group.MingruiOperation;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotNull;

/**
 * @ClassName SpecGroupDTO
 * @Description: TODO
 * @Author xuezhaochang
 * @Date 2021/1/4
 * @Version V1.0
 **/
@Data
@ApiModel(value = "规格组数据传输DTO")
public class SpecGroupDTO {
    @ApiModelProperty(value = "主键",example = "1")
    @NotNull(message = "主键不能为空",groups = {MingruiOperation.update.class})
    private Integer id;

    @ApiModelProperty(value = "类型的Id",example = "1")
    @NotNull(message = "类型Id不能为空",groups = {MingruiOperation.add.class})
    private Integer cid;

    @ApiModelProperty(value = "规格组名称")
    @NotNull(message = "名称不能为空",groups = {MingruiOperation.add.class})
    private String name;
}
