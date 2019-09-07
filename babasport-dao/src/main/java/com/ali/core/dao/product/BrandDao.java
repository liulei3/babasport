package com.ali.core.dao.product;

import com.ali.core.pojo.product.Brand;
import com.ali.core.pojo.product.BrandQuery;

import cn.itcast.common.page.Pagination;

import java.util.List;
import org.apache.ibatis.annotations.Param;

public interface BrandDao {

    int insert(Brand record);

    int insertSelective(Brand record);

    List<Brand> selectBrandListByQuery(BrandQuery brandQuery);

    Brand selectBrandById(Integer id);

    Pagination selectBrandPageByQuery(Integer pageNo,String name,Integer isDisplay);
    
    void updateBrandById(Brand brand);
    
    void deleteByIds(Long[] ids);
    
    int selectBrandTotalNo(BrandQuery brandQuery);
    
}