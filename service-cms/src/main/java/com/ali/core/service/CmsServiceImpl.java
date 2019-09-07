package com.ali.core.service;

import java.util.List;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import com.ali.core.dao.product.ColorDao;
import com.ali.core.dao.product.ProductDao;
import com.ali.core.dao.product.SkuDao;
import com.ali.core.pojo.product.Product;
import com.ali.core.pojo.product.Sku;
import com.ali.core.pojo.product.SkuQuery;
import com.ali.core.pojo.product.SkuQuery.Criteria;

/**
 * 用于展示商品详情
 * 包括三部分:获取指定商品,获取颜色,库存,价格信息
 * @author +u刘磊
 * @CreateDate 2016年5月13日
 */
@Service("cmsService")
public class CmsServiceImpl implements CmsService {
	@Resource
	private ProductDao productDao;
	@Resource
	private SkuDao skuDao;
	@Resource
	private ColorDao colorDao;
	
	// 根据id获取商品对象
	public Product selectProductById(Long id){
		return productDao.selectByPrimaryKey(id);
	}
	
	// 根据商品获取库存信息
	public List<Sku> selectSkuByProductId(Long id){
		SkuQuery skuQuery = new SkuQuery();
		// 查询
		Criteria criteria = skuQuery.createCriteria();
		// id
		criteria.andProductIdEqualTo(id);
		// 库存大于0
		criteria.andStockGreaterThan(0);
		
		List<Sku> skus = skuDao.selectByExample(skuQuery);
		for (Sku sku : skus) {
			// 通过colorId获取color对象
			sku.setColor(colorDao.selectByPrimaryKey(sku.getColorId()));
		}
		return skus;
	}
	

}
