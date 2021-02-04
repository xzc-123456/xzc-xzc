package com.baidu.shop.service.impl;

import com.baidu.shop.base.BaseApiService;
import com.baidu.shop.base.Result;
import com.baidu.shop.dto.BrandDTO;
import com.baidu.shop.entity.BrandEntity;
import com.baidu.shop.entity.CategoryBrandEntity;
import com.baidu.shop.mapper.BrandMapper;
import com.baidu.shop.mapper.CategoryBrandMapper;
import com.baidu.shop.service.BrandService;
import com.baidu.shop.utils.BaiduBeanUtil;
import com.baidu.shop.utils.ObjectUtils;
import com.baidu.shop.utils.PinyinUtil;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.google.gson.JsonObject;
import io.swagger.models.auth.In;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.RestController;
import tk.mybatis.mapper.entity.Example;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @ClassName BrandServiceImpl
 * @Description: TODO
 * @Author xuezhaochang
 * @Date 2020/12/25
 * @Version V1.0
 **/
@RestController
public class BrandServiceImpl extends BaseApiService implements BrandService {
    @Resource
    private BrandMapper brandMapper;
    @Resource
    private CategoryBrandMapper categoryBrandMapper;

    @Override
    public Result<PageInfo<BrandEntity>> getBrandInfo(BrandDTO brandDTO) {
        //分页
        PageHelper.startPage(brandDTO.getPage(),brandDTO.getRows());
        //排序
        if (!StringUtils.isEmpty(brandDTO.getSort())) PageHelper.orderBy(brandDTO.getOrderBy());

        BrandEntity brandEntity = BaiduBeanUtil.copyProperties(brandDTO,BrandEntity.class);

        Example example = new Example(BrandEntity.class);
        example.createCriteria().andLike("name","%" + brandEntity.getName() + "%");

        List<BrandEntity> brandEntities = brandMapper.selectByExample(example);
        PageInfo<BrandEntity> pageInfo = new PageInfo<>(brandEntities);

        return this.setResultSuccess(pageInfo);


    }
    //新增品牌列表
    @Transactional
    @Override
    public Result<JsonObject> saveBrandInfo(BrandDTO brandDTO) {
        //新增返回主键?
        //两种方式实现 select-key insert加两个属性
        BrandEntity brandEntity = BaiduBeanUtil.copyProperties(brandDTO, BrandEntity.class);
        //处理品牌首字母
       brandEntity.setLetter(PinyinUtil.getUpperCase(String.valueOf(brandEntity.getName().toCharArray()[0]),false).toCharArray()[0]);
       brandMapper.insertSelective(brandEntity);
       //维护中间表
        String categories = brandDTO.getCategories();//得到分类集合字符串
        //批量新增.新增
        this.saveCatryBrandInfo(brandDTO.getCategories(),brandEntity.getId());
        return this.setResultSuccess();
    }
    @Transactional
    @Override
    public Result<JsonObject> editBrandInfo(BrandDTO brandDTO) {
        BrandEntity brandEntity = BaiduBeanUtil.copyProperties(brandDTO, BrandEntity.class);
        brandEntity.setLetter(PinyinUtil.getUpperCase(String.valueOf(brandEntity.getName().toCharArray()[0]),false).toCharArray()[0]);
        brandMapper.updateByPrimaryKeySelective(brandEntity);
            //先通过brandId删除中间表的数据
        this.publicFunctiondelete(brandDTO.getId());
        //批量新增 /新增
        this.saveCatryBrandInfo(brandDTO.getCategories(),brandEntity.getId());

        return this.setResultSuccess();
    }
    @Transactional
    @Override
    public Result<JsonObject> deleteBrandInfo(Integer id) {
        //删除品牌
        brandMapper.deleteByPrimaryKey(id);
        //删除品牌关联的分类
        this.publicFunctiondelete(id);
        return this.setResultSuccess();
    }

    @Override
    public Result<List<BrandEntity>> getBrandByCategory(Integer cid) {
            List<BrandEntity> list = brandMapper.getBrandByCategory(cid);
        return this.setResultSuccess(list);
    }

    //封装删除关系表
    private void publicFunctiondelete(Integer brandId){
        Example example = new Example(CategoryBrandEntity.class);
        example.createCriteria().andEqualTo("brandId",brandId);
        categoryBrandMapper.deleteByExample(example);
    }



    //封装批量新增和新增
    private void saveCatryBrandInfo(String categories, Integer brandId){

        if (StringUtils.isEmpty(categories)) throw new RuntimeException("分类不能为空");

        if (categories.contains(",")){
            categoryBrandMapper.insertList(
                    Arrays.asList(categories.split(","))
                            .stream()
                            .map(categoryIdStr -> new CategoryBrandEntity(Integer.valueOf(categoryIdStr)
                                    ,brandId))
                            .collect(Collectors.toList())
            );
        }else {
            CategoryBrandEntity categoryBrandEntity = new CategoryBrandEntity();
            categoryBrandEntity.setCategoryId(Integer.valueOf(categories));
            categoryBrandEntity.setBrandId(brandId);

            categoryBrandMapper.insertSelective(categoryBrandEntity);
        }
    }
}
