package com.baidu.shop.dto;

import com.baidu.shop.base.BaseDTO;
import com.baidu.shop.validate.group.MingruiOperation;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.Value;

import javax.validation.constraints.NotNull;

/**
 * @ClassName SpecParamDTO
 * @Description: TODO
 * @Author xuezhaochang
 * @Date 2021/1/4
 * @Version V1.0
 **/
@ApiModel(value = "规格参数数据传输DTO")
@Data
public class SpecParamDTO extends BaseDTO {
    @ApiModelProperty(value = "主键",example = "1")
    @NotNull(message = "主键不能为空",groups = {MingruiOperation.update.class})
    private Integer id;

    @ApiModelProperty(value="分类Id",example = "1")
    private Integer cid;

    @ApiModelProperty(value = "规格组Id",example = "1")
    private Integer groupId;

    @ApiModelProperty(value="规格参数名称")
    private String name;

    @ApiModelProperty(value = "是否是数值类型的单位,1为true,0为false",example = "0")
    @NotNull(message = "是否是数值类型不能为空",
            groups = {MingruiOperation.add.class,MingruiOperation.update.class})
    private Boolean numeric;

    @ApiModelProperty(value = "数字类型参数的单位，非数字类型可以为空")
    private String unit;

    @ApiModelProperty(value = "是否是sku通用属性，1为true0为false",example = "0")
    @NotNull(message = "是否是sku通用属性不能为空",groups = {MingruiOperation.add.class,
            MingruiOperation.update.class})
    private Boolean generic;

    @ApiModelProperty(value = "是否用于搜索过滤，1为true0为false",example = "0")
    @NotNull(message = "是否用于搜索过滤不能为空",groups = {MingruiOperation.add.class,
            MingruiOperation.update.class})
    private Boolean searching;

    @ApiModelProperty(value = "数值类型参数，如果需要搜索，则添加分段间隔值，如CPU频率间 隔：0.5-1.0")
    private String segments;





}
