package com.baidu.shop.service.impl;

import com.baidu.shop.base.BaseApiService;
import com.baidu.shop.base.Result;
import com.baidu.shop.dto.SpecParamDTO;
import com.baidu.shop.entity.SpecParamEntity;
import com.baidu.shop.mapper.SpecParamMapper;
import com.baidu.shop.service.SpecificationService;
import com.baidu.shop.utils.BaiduBeanUtil;
import com.baidu.shop.utils.ObjectUtils;
import com.baidu.shop.validate.group.MingruiOperation;
import com.google.gson.JsonObject;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.RestController;
import tk.mybatis.mapper.entity.Example;

import javax.annotation.Resource;
import java.util.List;

/**
 * @ClassName SpecParamsServiceImpl
 * @Description: TODO
 * @Author xuezhaochang
 * @Date 2021/1/4
 * @Version V1.0
 **/
@RestController
public class SpecParamsServiceImpl extends BaseApiService implements SpecificationService {
    @Resource
    private SpecParamMapper specParamMapper;

    @Override
    public Result<List<SpecParamEntity>> getSpecParamInfo(SpecParamDTO specParamDTO) {
        SpecParamEntity specParamEntity = BaiduBeanUtil.copyProperties(specParamDTO, SpecParamEntity.class);
        Example example = new Example(SpecParamEntity.class);
        Example.Criteria criteria = example.createCriteria();
        if (ObjectUtils.isNotNull(specParamEntity.getGroupId()))
            criteria.andEqualTo("groupId",specParamEntity.getGroupId());
        if (ObjectUtils.isNotNull(specParamEntity.getCid()))
            criteria.andEqualTo("cid",specParamEntity.getCid());
        List<SpecParamEntity> list = specParamMapper.selectByExample(example);
        return this.setResultSuccess(list);
    }
    @Transactional
    @Override
    public Result<JsonObject> saveSpecParamInfo(SpecParamDTO specParamDTO) {
        specParamMapper.insertSelective(BaiduBeanUtil.copyProperties(specParamDTO,SpecParamEntity.class));
        return this.setResultSuccess();
    }
    @Transactional
    @Override
    public Result<JsonObject> editSpecParamInfo(SpecParamDTO specParamDTO) {
        specParamMapper.updateByPrimaryKeySelective(BaiduBeanUtil.copyProperties(specParamDTO,SpecParamEntity.class));
        return this.setResultSuccess();
    }
    @Transactional
    @Override
    public Result<JsonObject> delSpecParamInfo(Integer id) {

        specParamMapper.deleteByPrimaryKey(id);
        return this.setResultSuccess();
    }
}
