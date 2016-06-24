package qinglian.zeng.practice.kata01.supermarketPricing;

import java.util.Map;

import qinglian.zeng.practice.kata01.supermarketPricing.product.Product;

public interface PricingManager
{
    long checkOut( Map<Product, Number> products );
}
