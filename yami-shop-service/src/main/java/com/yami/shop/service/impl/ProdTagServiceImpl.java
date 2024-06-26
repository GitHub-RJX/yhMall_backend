package com.yami.shop.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.yami.shop.bean.model.ProdTag;
import com.yami.shop.dao.ProdTagMapper;
import com.yami.shop.service.ProdTagService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * 商品分组标签
 *
 * @author hzm
 * @date 2019-04-18 10:48:44
 */
@Service
public class ProdTagServiceImpl extends ServiceImpl<ProdTagMapper, ProdTag> implements ProdTagService {

    @Autowired
    private ProdTagMapper prodTagMapper;

    @Override
    @Cacheable(cacheNames = "prodTag", key = "'prodTag'")
    public List<ProdTag> listProdTag() {
        return prodTagMapper.selectList(new LambdaQueryWrapper<ProdTag>()
                .eq(ProdTag::getStatus, 1)
                .orderByAsc(ProdTag::getId));
    }

    @Override
    @CacheEvict(cacheNames = "prodTag", key = "'prodTag'")
    public void removeProdTag() {
    }
}
