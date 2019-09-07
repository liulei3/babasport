package com.ali.core.service.product;

import java.util.List;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import com.ali.core.dao.product.ColorDao;
import com.ali.core.dao.product.SkuDao;
import com.ali.core.pojo.product.Sku;
import com.ali.core.pojo.product.SkuQuery;
import com.ali.core.pojo.product.SkuQuery.Criteria;

@Service("skuService")
public class SkuServiceImpl implements SkuService{

	@Resource
	private SkuDao skuDao;
	@Resource
	private ColorDao colorDao;

	/**
	 * 新增商品
	 *  添加商品,添加库存
	 */
	public void insert(Sku sku) {
		skuDao.insertSelective(sku);
	}

	/**
	 * 条件查询库存
	 */
	public List<Sku> selectByExample(Long productId) {
		SkuQuery skuQuery = new SkuQuery();
		Criteria criteria = skuQuery.createCriteria();
		
		// 不用判断productId是否存在,由于根据id进入库存,所以不用判断
		criteria.andProductIdEqualTo(productId);
		
		List<Sku> skus = skuDao.selectByExample(skuQuery);
		// 获取库存对象的颜色
		for (Sku sku : skus) {
			sku.setColor(colorDao.selectByPrimaryKey(sku.getColorId()));
		}
		return skus;
	}
	
	/**
	 * 根据id保存库存
	 */
	public void updateByPrimaryKeySelective(Sku sku) {
		skuDao.updateByPrimaryKeySelective(sku);
	}	
}
