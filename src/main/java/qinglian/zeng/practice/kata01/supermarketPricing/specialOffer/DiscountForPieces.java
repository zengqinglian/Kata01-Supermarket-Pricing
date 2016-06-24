package qinglian.zeng.practice.kata01.supermarketPricing.specialOffer;

import java.util.HashSet;
import java.util.Set;

import qinglian.zeng.practice.kata01.supermarketPricing.product.Product;
import qinglian.zeng.practice.kata01.supermarketPricing.product.Product.SellingType;

import com.sun.istack.internal.NotNull;

public class DiscountForPieces
{
    private final int id;// rule id
    private Set<Product> products = new HashSet<>();;
    private DiscountType type;

    public DiscountForPieces( int id, @NotNull DiscountType type ) {
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

    public void addProduct( Product product ) {
        if( product.getType() == SellingType.PIECES ) {
            products.add( product );
        } else {
            throw new UnsupportedOperationException();
        }
    }

    public Set<Product> getProducts() {
        return products;
    }

    public void clearProducts() {
        products.clear();
    }

    public static enum DiscountType
    {
        HALFPRICE {
            public long getTotalPrice( long unitPrice ) {
                return unitPrice / 2; // round down
            }

            public int getReducePieces() {
                return 1;
            }

            public String getName() {
                return "Half price";
            }
        },
        THREEFORTWO {
            public long getTotalPrice( long unitPrice ) {
                return 2 * unitPrice;
            }

            public int getReducePieces() {
                return 3;
            }

            public String getName() {
                return "Three for two";
            }
        },
        BUGONEGETONEFREE {

            @Override
            public long getTotalPrice( long unitPrice ) {

                return unitPrice;
            }

            @Override
            public int getReducePieces() {
                return 2;
            }

            public String getName() {
                return "Buy one get one free";
            }

        },
        BUYONEGETONEHALFPRICE {

            @Override
            public long getTotalPrice( long unitPrice ) {

                return unitPrice + unitPrice / 2; // round down
            }

            @Override
            public int getReducePieces() {
                return 2;
            }

            @Override
            public String getName() {
                return "Buy one get one half price";
            }
        };

        public abstract long getTotalPrice( long unitPrice );

        public abstract int getReducePieces();

        public abstract String getName();
    }
}
