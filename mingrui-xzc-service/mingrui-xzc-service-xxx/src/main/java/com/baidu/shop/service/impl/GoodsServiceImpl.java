package com.baidu.shop.service.impl;

import com.baidu.shop.base.BaseApiService;
import com.baidu.shop.base.Result;
import com.baidu.shop.dto.SkuDTO;
import com.baidu.shop.dto.SpuDTO;
import com.baidu.shop.dto.SpuDetailDTO;
import com.baidu.shop.entity.*;
import com.baidu.shop.mapper.*;
import com.baidu.shop.service.GoodsService;
import com.baidu.shop.status.HTTPStatus;
import com.baidu.shop.utils.BaiduBeanUtil;
import com.baidu.shop.utils.ObjectUtils;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.google.gson.JsonObject;
import lombok.extern.slf4j.Slf4j;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.RestController;
import tk.mybatis.mapper.entity.Example;

import javax.annotation.Resource;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @ClassName GoodsServiceImpl
 * @Description: TODO
 * @Author xuezhaochang
 * @Date 2021/1/5
 * @Version V1.0
 **/
@RestController
@Slf4j
public class GoodsServiceImpl extends BaseApiService implements GoodsService {

    @Resource
    private SpuMapper spuMapper;
    @Resource
    private BrandMapper brandMapper;
    @Resource
    private CategoryMapper categoryMapper;
    @Resource
    private SpuDetailMapper spuDetailMapper;
    @Resource
    private SkuMapper skuMapper;
    @Resource
    private StockMapper stockMapper;


    @Override
    public Result<List<SpuDTO>> getSpuInfo(SpuDTO spuDTO) {
        //判断分页参数是否为空
        if (ObjectUtils.isNotNull(spuDTO.getPage()) && ObjectUtils.isNotNull(spuDTO.getRows()))
            //如果不为空.赋值
            PageHelper.startPage(spuDTO.getPage(), spuDTO.getRows());
        //实现排序,判断两个字段不为空
        if (!StringUtils.isEmpty(spuDTO.getSort()) && !StringUtils.isEmpty(spuDTO.getOrder()))
            PageHelper.orderBy(spuDTO.getOrderBy());
        //将spuEntity放入Example
        Example example = new Example(SpuEntity.class);
        //new 一个 Example.Criteria,目的是为了方便以后万一有条件判断的话,可以直接抽取出来直接使用
        Example.Criteria criteria = example.createCriteria();
        //判断上架,下架是否为空
        if (ObjectUtils.isNotNull(spuDTO.getSaleable()) && spuDTO.getSaleable() < 2)
            //不为空,赋值
            criteria.andEqualTo("saleable", spuDTO.getSaleable());
        //判断标题是否为空,用作模糊查询
        if (!StringUtils.isEmpty(spuDTO.getTitle()))
            //不为空进行模糊查询
            criteria.andLike("title", "%" + spuDTO.getTitle() + "%");

        //把查询结果都放在List集合中
        List<SpuEntity> spuEntities = spuMapper.selectByExample(example);

        List<SpuDTO> spuDTOList = spuEntities.stream().map(spuEntity -> {
            SpuDTO spuDTO1 = BaiduBeanUtil.copyProperties(spuEntity, SpuDTO.class);
            //通过分类Id集合查询数据
            List<CategoryEntity> categoryEntities = categoryMapper.selectByIdList(Arrays.asList(spuEntity.getCid1(), spuEntity.getCid2(), spuEntity.getCid3()));
            //遍历集合,并且将分类名称用/分割
            String categoryName = categoryEntities.stream().map(categoryEntity -> categoryEntity.getName()).collect(Collectors.joining("/"));
            spuDTO1.setCategoryName(categoryName);
            BrandEntity brandEntity = brandMapper.selectByPrimaryKey(spuEntity.getBrandId());
            spuDTO1.setBrandName(brandEntity.getName());
            return spuDTO1;
        }).collect(Collectors.toList());

        //PageInfo整理集合的信息
        PageInfo<SpuEntity> spuEntityPageInfo = new PageInfo<>(spuEntities);
        //返回
        return this.setResult(HTTPStatus.OK, spuEntityPageInfo.getTotal() + "", spuDTOList);
    }

    //新增商品
    @Override
    @Transactional
    public Result<JsonObject> saveGoods(SpuDTO spuDTO) {
        log.info("{}", spuDTO);
        final Date date = new Date();
        //新增spu,返回主键,给必要字段赋默认值
        SpuEntity spuEntity = BaiduBeanUtil.copyProperties(spuDTO, SpuEntity.class);
        spuEntity.setSaleable(1);
        spuEntity.setValid(1);
        spuEntity.setCreateTime(date);
        spuEntity.setLastUpdateTime(date);
        spuMapper.insertSelective(spuEntity);
        //新增spudetail
        SpuDetailDTO spuDetail = spuDTO.getSpuDetail();
        SpuDetailEntity spuDetailEntity = BaiduBeanUtil.copyProperties(spuDetail, SpuDetailEntity.class);
        spuDetailEntity.setSpuId(spuEntity.getId());
        spuDetailMapper.insertSelective(spuDetailEntity);

        //新增sku  list是插入顺序有序,比如先插入b,就先取出b,set插入取出顺序是无序的,先插入a,取出的不一定是a
        this.saveSkusAndStockInfo(spuDTO, spuEntity.getId(), date);

        return this.setResultSuccess();
    }

    public void saveSkusAndStockInfo(SpuDTO spuDTO, Integer spuId, Date date) {
        List<SkuDTO> skus = spuDTO.getSkus();
        skus.stream().forEach(skuDTO -> {

            SkuEntity skuEntity = BaiduBeanUtil.copyProperties(skuDTO, SkuEntity.class);
            skuEntity.setSpuId(spuId);
            skuEntity.setCreateTime(date);
            skuEntity.setLastUpdateTime(date);
            skuMapper.insertSelective(skuEntity);

            //新增stock
            StockEntity stockEntity = new StockEntity();
            stockEntity.setSkuId(skuEntity.getId());
            stockEntity.setStock(skuDTO.getStock());
            stockMapper.insertSelective(stockEntity);
        });
    }

    @Override
    public Result<SpuDetailEntity> getSpudetailBySpuId(Integer spuId) {
        SpuDetailEntity spuDetailEntity = spuDetailMapper.selectByPrimaryKey(spuId);
        return this.setResultSuccess(spuDetailEntity);
    }

    @Override
    public Result<List<SkuDTO>> getSkusBySpuId(Integer spuId) {
        List<SkuDTO> list = skuMapper.getSkusAndStockBySpuId(spuId);
        return this.setResultSuccess(list);
    }

    @Override
    @Transactional
    public Result<JsonObject> editSkusBySpuId(SpuDTO spuDTO) {
        final Date date = new Date();
        //通过spuId修改
        SpuEntity spuEntity = BaiduBeanUtil.copyProperties(spuDTO, SpuEntity.class);
        spuEntity.setLastUpdateTime(date);
        spuMapper.updateByPrimaryKeySelective(spuEntity);

        //通过spuId修改spudetail
        spuDetailMapper.updateByPrimaryKeySelective(BaiduBeanUtil
                .copyProperties(spuDTO.getSpuDetail()
                        , SpuDetailEntity.class));
        //通过spuId查询sku信息
        this.deleteSkusAndStock(spuEntity.getId());
        this.saveSkusAndStockInfo(spuDTO, spuEntity.getId(), date);
        return this.setResultSuccess();
    }

    @Override
    @Transactional
    public Result<JsonObject> deleteGoods(Integer spuId) {
        spuMapper.deleteByPrimaryKey(spuId);
        spuDetailMapper.deleteByPrimaryKey(spuId);
        this.deleteSkusAndStock(spuId);
        return this.setResultSuccess();
    }

    @Override
    public Result<JsonObject> shangjiaOrxiajia(SpuDTO spuDTO) {
        SpuEntity spuEntity = BaiduBeanUtil.copyProperties(spuDTO, SpuEntity.class);
        if (ObjectUtils.isNotNull(spuEntity.getSaleable()) && spuEntity.getSaleable() != 2){
            if (spuEntity.getSaleable() == 1){
                spuEntity.setSaleable(0);
                spuMapper.updateByPrimaryKey(spuEntity);
                return this.setResultSuccess("已下架");
            }else {
                spuEntity.setSaleable(1);
                spuMapper.updateByPrimaryKey(spuEntity);
                return this.setResultSuccess("已上架");
            }
        }
        return this.setResultError("失败");
    }

    public void deleteSkusAndStock(Integer spuId){
        Example example = new Example(SkuEntity.class);
        example.createCriteria().andEqualTo("spuId", spuId);
        List<SkuEntity> skuEntities = skuMapper.selectByExample(example);
        //得到skuId集合
        List<Long> skuIdList = skuEntities.stream().map(skuEntity -> skuEntity.getId()).collect(Collectors.toList());
        skuMapper.deleteByIdList(skuIdList);
        stockMapper.deleteByIdList(skuIdList);
    }
}

