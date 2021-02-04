package com.baidu.shop.entity;

import lombok.Data;
import org.springframework.validation.annotation.Validated;

import javax.persistence.Id;
import javax.persistence.Table;

/**
 * @ClassName SpuDetailEntity
 * @Description: TODO
 * @Author xuezhaochang
 * @Date 2021/1/7
 * @Version V1.0
 **/
@Table(name="tb_spu_detail")
@Data
public class SpuDetailEntity {
    @Id
    private Integer spuId;

    private String description;

    private String genericSpec;

    private String specialSpec;

    private String packingList;

    private String afterService;
}
