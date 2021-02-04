package com.baidu.shop.dto;

import com.baidu.shop.base.BaseDTO;
import com.baidu.shop.validate.group.MingruiOperation;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import io.swagger.models.auth.In;
import lombok.Data;

import javax.validation.constraints.NotNull;

/**
 * @ClassName BrandDTO
 * @Description: TODO
 * @Author xuezhaochang
 * @Date 2020/12/25
 * @Version V1.0
 **/
@Data
@ApiModel(value = "品牌DTO")
public class BrandDTO extends BaseDTO {
    @ApiModelProperty(value = "品牌主键",example = "1")
    @NotNull(message = "主键不能为空",groups = {MingruiOperation.update.class})
    private Integer id;

    @ApiModelProperty(value = "品牌名称")
    @NotNull(message = "品牌名称不能为空",groups = {MingruiOperation.add.class,MingruiOperation.update.class})
    private String name;

    @ApiModelProperty(value = "品牌图片")
    private String image;

    @ApiModelProperty(value = "品牌首字母")
    private Character letter;

    @ApiModelProperty(value = "品牌id集合")
    private String categories;
}
