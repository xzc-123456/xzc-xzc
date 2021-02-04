package com.baidu.shop.dto;

import com.baidu.shop.base.BaseDTO;
import com.baidu.shop.validate.group.MingruiOperation;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import io.swagger.models.auth.In;
import lombok.Data;

import javax.management.ValueExp;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.util.Date;
import java.util.List;

/**
 * @ClassName SpuDTO
 * @Description: TODO
 * @Author xuezhaochang
 * @Date 2021/1/5
 * @Version V1.0
 **/
@ApiModel(value = "SPU数据传输DTO")
@Data
public class SpuDTO extends BaseDTO {
    @ApiModelProperty(value = "主键",example = "1")
    @NotNull(message = "主键不能为空",groups = {MingruiOperation.update.class})
    private Integer id;

    @ApiModelProperty(value = "标题")
    @NotEmpty(message = "标题不能为空",groups = {MingruiOperation.add.class})
    private String title;

    @ApiModelProperty(value = "子标签")
    private String subTitle;

    @ApiModelProperty(value = "1级类目id",example = "1")
    @NotNull(message = "1级类目不能为空",groups = {MingruiOperation.add.class})
    private Integer cid1;

    @ApiModelProperty(value = "2级类目id",example = "1")
    @NotNull(message = "2级类目不能为空",groups = {MingruiOperation.add.class})
    private Integer cid2;

    @ApiModelProperty(value = "3级类目id",example = "1")
    @NotNull(message = "3级类目不能为空",groups = {MingruiOperation.add.class})
    private Integer cid3;

    @ApiModelProperty(value = "商品所属品牌id",example = "1")
    @NotNull(message = "商品所属品牌id不能为空",groups = {MingruiOperation.add.class})
    private Integer brandId;

    @ApiModelProperty(value = "是否上架，0下架，1上架",example = "1")
    private Integer saleable;

    @ApiModelProperty(value = "是否有效，0已删除，1有效",example = "1")
    private Integer valid;

    @ApiModelProperty(value = "添加时间")
    private Date createTime;

    @ApiModelProperty(value = "最后修改时间")
    private Date lastUpdateTime;

    private String categoryName;

    private String brandName;

    @ApiModelProperty(value = "大字段数据")
    private SpuDetailDTO spuDetail;

    @ApiModelProperty(value = "sku属性数据集合")
    private List<SkuDTO> skus;
}