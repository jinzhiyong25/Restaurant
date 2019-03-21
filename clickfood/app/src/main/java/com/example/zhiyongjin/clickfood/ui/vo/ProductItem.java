package com.example.zhiyongjin.clickfood.ui.vo;

import com.example.zhiyongjin.clickfood.bean.Product;

public class ProductItem extends Product {

    public int count;

    public ProductItem(Product product) {
        this.id = product.getId();
        this.name = product.getName();
        this.label = product.getLabel();
        this.description = product.getDescription();
        this.price = product.getPrice();
        this.icon = product.getIcon();
        this.restaurant = product.getRestaurant();
    }
}
