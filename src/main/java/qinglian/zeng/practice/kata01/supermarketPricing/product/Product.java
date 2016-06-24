package qinglian.zeng.practice.kata01.supermarketPricing.product;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.HashCodeBuilder;

import qinglian.zeng.practice.kata01.supermarketPricing.specialOffer.DiscountForPieces;
import qinglian.zeng.practice.kata01.supermarketPricing.specialOffer.DiscountForWeight;

public class Product<E>
{
    private final int id;
    private final String name;
    private long unitPrice; // price in pence
    private final SellingType type;
    private List<E> offers = new ArrayList<>();// Discount will be considered in order
    private ProductGroup group; // TODO: many-to-many

    public Product( int id, String name, long unitPrice, SellingType type ) {
        this.id = id;
        this.name = name;
        this.unitPrice = unitPrice;
        this.type = type;
    }

    public void addOffer( E offer ) {
        if( (type == SellingType.PIECES && offer instanceof DiscountForPieces)
                || (type == SellingType.WEIGHT && offer instanceof DiscountForWeight) ) {
            offers.add( offer );
        } else {
            throw new UnsupportedOperationException();
        }
    }

    public void removeOffer( E offer ) {
        if( !offers.remove( offer ) ) {
            throw new UnsupportedOperationException();
        }
    }

    public List<E> getOffers() {
        return offers;
    }

    public void cleanOffers() {
        offers.clear();
    }

    public SellingType getType() {
        return type;
    }

    public long getUnitPrice() {
        return unitPrice;
    }

    public void setUnitPrice( long unitPrice ) {
        this.unitPrice = unitPrice;
    }

    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public ProductGroup getGroup() {
        return group;
    }

    public void setGroup( ProductGroup group ) {
        this.group = group;
    }

    @Override
    public int hashCode() {
        HashCodeBuilder builder = new HashCodeBuilder();
        builder.append( getId() );
        return builder.hashCode();
    }

    @Override
    public boolean equals( Object obj ) {
        if( !(obj instanceof Product) ) {
            return false;
        }
        Product other = (Product) obj;
        EqualsBuilder builder = new EqualsBuilder();
        builder.append( getId(), other.getId() );
        return builder.isEquals();
    }

    public static enum SellingType
    {
        PIECES {

            @Override
            public Number plus( Number v1, Number v2 ) {
                return v1.intValue() + v2.intValue();
            }

            @Override
            public Number minus( Number v1, Number v2 ) {
                return v1.intValue() - v2.intValue();
            }

            @Override
            public boolean equalOrGreaterThan( Number v1, Number v2 ) {

                return v1.intValue() >= v2.intValue();
            }

            @Override
            public boolean isZero( Number value ) {

                return value.intValue() == 0;
            }

        },
        WEIGHT {

            @Override
            public Number plus( Number v1, Number v2 ) {
                return v1.doubleValue() + v2.doubleValue();
            }

            @Override
            public Number minus( Number v1, Number v2 ) {
                return v1.doubleValue() - v2.doubleValue();
            }

            @Override
            public boolean equalOrGreaterThan( Number v1, Number v2 ) {
                return v1.doubleValue() >= v2.doubleValue();
            }

            @Override
            public boolean isZero( Number value ) {
                return value.doubleValue() == 0;
            }

        };

        public abstract Number plus( Number v1, Number v2 );

        public abstract Number minus( Number v1, Number v2 );

        public abstract boolean equalOrGreaterThan( Number v1, Number v2 );

        public abstract boolean isZero( Number value );
    }
}
