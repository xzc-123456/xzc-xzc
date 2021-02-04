package com.baidu.shop.base;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import io.swagger.annotations.ApiOperation;
import lombok.Data;
import lombok.Value;
import org.springframework.util.StringUtils;

/**
 * @ClassName BaseDTO
 * @Description: TODO
 * @Author xuezhaochang
 * @Date 2020/12/25
 * @Version V1.0
 **/
@Data
@ApiModel(value = "BaseDTO用于传输数据，其他DTO需要继承此类")
public class BaseDTO {
    @ApiModelProperty(value = "当前页",example = "1")
    private Integer page;

    @ApiModelProperty(value = "每页显示多少条",example = "5")
    private Integer rows;

    @ApiModelProperty(value = "排序字段")
    private String sort;

    @ApiModelProperty(value = "是否升序")
    private String order;

    public String getOrderBy(){
        return sort + " " + (Boolean.valueOf(order)? "desc" : "asc");
    }
}
