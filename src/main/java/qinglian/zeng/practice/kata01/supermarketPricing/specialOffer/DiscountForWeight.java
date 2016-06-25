package qinglian.zeng.practice.kata01.supermarketPricing.specialOffer;

import java.util.HashSet;
import java.util.Set;

import org.apache.commons.lang.Validate;

import qinglian.zeng.practice.kata01.supermarketPricing.product.Product;
import qinglian.zeng.practice.kata01.supermarketPricing.product.Product.SellingType;

public class DiscountForWeight
{
    private final int id;// rule id
    private final Set<Product<DiscountForWeight>> products = new HashSet<>();
    private DiscountType type;

    public DiscountForWeight( int id, DiscountType type ) {
        Validate.notNull( type, "Discount type can not be null" );
        this.id = id;
        this.type = type;
    }

    public DiscountType getType() {
        return type;
    }

    public void setType( DiscountType type ) {
        this.type = type;
    }

    public int getId() {
        return id;
    }

    public Set<Product<DiscountForWeight>> getProducts() {
        return products;
    }

    public void addProduct( Product<DiscountForWeight> product ) {
        if( product.getType() == SellingType.WEIGHT ) {
            products.add( product );
            product.addOffer( this );
        } else {
            throw new UnsupportedOperationException();
        }
    }

    public void clearProducts() {
        for( Product<DiscountForWeight> product : products ) {
            product.removeOffer( this );
        }
        products.clear();
    }

    public static enum DiscountType
    {
        // it is not offten to see offer for weight , so i made sth up. This mode might not work for some real scenario
        HALFPRICE {

            @Override
            public long getTotalPrice( long unitPrice, double weight ) {
                return Math.round( unitPrice / 2 * weight ); // round to close
            }

            @Override
            public double getMinWeight() {
                return 0; // unit is KG
            }

            @Override
            public String getName() {
                return "Half price";
            }

        };
        public abstract long getTotalPrice( long unitPrice, double weight );

        public abstract double getMinWeight();

        public abstract String getName();
    }

}
