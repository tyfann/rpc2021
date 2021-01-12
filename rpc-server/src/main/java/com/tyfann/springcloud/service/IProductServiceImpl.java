package com.tyfann.springcloud.service;

import com.tyfann.springcloud.entities.IProductService;
import com.tyfann.springcloud.entities.Product;

/**
 * @author tyfann
 * @date 2021/1/12 8:44 下午
 */
public class IProductServiceImpl implements IProductService {
    @Override
    public Product findProductById(Integer id) {
        return new Product(id,"Mutton",10);
    }

    @Override
    public String findProductInfoById(Integer id) {
        return id+"Mutton";
    }


}
