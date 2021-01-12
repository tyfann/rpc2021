package com.tyfann.springcloud.entities;

/**
 * @author tyfann
 * @date 2021/1/12 7:59 下午
 */
public interface IProductService {
    public Product findProductById(Integer id);

    public String findProductInfoById(Integer id);
}
