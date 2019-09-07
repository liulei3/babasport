package com.ali.core.service;

import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import com.ali.core.dao.product.ColorDao;
import com.ali.core.dao.product.ProductDao;
import com.ali.core.dao.product.SkuDao;
import com.ali.core.dao.user.BuyerDao;
import com.ali.core.pojo.cart.BuyerItem;
import com.ali.core.pojo.cart.Cart;
import com.ali.core.pojo.product.Sku;
import com.ali.core.pojo.user.Buyer;

import redis.clients.jedis.Jedis;

@Service("buyerService")
public class BuyerServiceImpl implements BuyerService{
	@Resource
	private BuyerDao buyerDao;
	@Resource
	private Jedis jedis;
	@Resource
	private SkuDao skuDao;
	@Resource
	private ColorDao colorDao;
	@Resource
	private ProductDao productDao;
	
	public Buyer selectBuyerByName(String name){
		// 从索引库中获取查询id,去数据库中查询
		String id = jedis.get(name);
		// 当用户名不正确时,返回null
		if(null == id){
			return null;
		}
		return buyerDao.selectByPrimaryKey(Long.parseLong(id));
	}
	
	// 查询购物项数据
	public Sku selectSkuById(Long skuId){
		Sku sku = skuDao.selectByPrimaryKey(skuId);
		sku.setColor(colorDao.selectByPrimaryKey(sku.getColorId()));
		sku.setProduct(productDao.selectByPrimaryKey(sku.getProductId()));
		return sku;
	}
	
	// 加入购物车,保证数据共享,放入redis中
	public void insertCart2Redis(Cart cart,String username){
		// 1 判断购物车是否存在
		if(null != cart){
			List<BuyerItem> items = cart.getItems();
			// 2 判断购物项是否存在
			if(items.size()>0){
				for (BuyerItem item : items) {
					// 3 判断redis中是否存在
					if(jedis.hexists("cart:"+username, 
							String.valueOf(item.getSku().getId()))){
						// 相同商品,进行数量相加
						jedis.hincrBy("cart:"+username, 
							String.valueOf(item.getSku().getId()),item.getAmount());
					}else{
						jedis.hset("cart:"+username, 
							String.valueOf(item.getSku().getId()),String.valueOf(item.getAmount()));
					}
				}
			}
		}
	}
	
	// 获取redis购物车
	public Cart selectCart4Redis(String name){
		// 1 创建购物车
		Cart cart = new Cart();
		// 2 判断用户在redis中是否有购物车
		Map<String, String> hgetAll = jedis.hgetAll("cart:"+name);
		if(null != hgetAll){
			// 3 获取redis中所有购物车
			Set<Entry<String, String>> entrySet = hgetAll.entrySet();
			for (Entry<String, String> entry : entrySet) {
				Sku sku = new Sku();
				sku.setId(Long.parseLong(entry.getKey()));
				// 将库存商品放入item中
				BuyerItem item = new BuyerItem();
				item.setSku(sku);
				item.setAmount(Integer.parseInt(entry.getValue()));
				// 4 获取数据
				cart.addItem(item);
			}
		}
		return cart;
	}
}
