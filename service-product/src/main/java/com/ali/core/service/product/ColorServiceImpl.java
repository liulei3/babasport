package com.ali.core.service.product;

import java.util.List;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import com.ali.core.dao.product.ColorDao;
import com.ali.core.pojo.product.Color;
import com.ali.core.pojo.product.ColorQuery;

@Service("colorService")
public class ColorServiceImpl implements ColorService {

	@Resource
	private ColorDao colorDao;
	
	/**
	 * 条件查询
	 */
	public List<Color> selectByExample() {
		ColorQuery colorQuery = new ColorQuery();
		// 查询所有颜色,排除色系(分类条件,不是具体颜色)
		colorQuery.createCriteria().andParentIdNotEqualTo(0L);		
		return colorDao.selectByExample(colorQuery);
	}
	
	
}
