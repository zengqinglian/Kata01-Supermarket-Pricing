package qinglian.zeng.practice.kata01.supermarketPricing;

import java.util.Map;

import qinglian.zeng.practice.kata01.supermarketPricing.product.Product;

/**
 * @author quinglian.zeng
 *
 */
public interface PricingManager
{
    /**
     * @param products
     * @return
     */
    long checkOut( Map<Product<?>, Number> products );
}
