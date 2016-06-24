package qinglian.zeng.practice.kata01.supermarketPricing.product;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.HashCodeBuilder;

import qinglian.zeng.practice.kata01.supermarketPricing.product.Product.SellingType;
import qinglian.zeng.practice.kata01.supermarketPricing.specialOffer.GroupBuyDiscount;

import com.sun.istack.internal.NotNull;

public class ProductGroup
{
    private final int id;
    private String name;
    private Set<Product> products = new HashSet<>();
    private List<GroupBuyDiscount> offers = new ArrayList<>();

    public ProductGroup( int id, @NotNull String name ) {
        this.id = id;
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public void setName( String name ) {
        this.name = name;
    }

    public Set<Product> getProducts() {
        return products;
    }

    public void addProducts( Product product ) {
        if( product.getType() == SellingType.PIECES ) {
            this.products.add( product );
            product.setGroup( this );
        } else {
            throw new UnsupportedOperationException();
        }
    }

    public List<GroupBuyDiscount> getOffers() {
        return offers;
    }

    public void addOffer( GroupBuyDiscount offer ) {
        offers.add( offer );
    }

    public int getId() {
        return id;
    }

    @Override
    public int hashCode() {
        HashCodeBuilder builder = new HashCodeBuilder();
        builder.append( getId() );
        return builder.hashCode();
    }

    @Override
    public boolean equals( Object obj ) {
        if( !(obj instanceof ProductGroup) ) {
            return false;
        }
        ProductGroup other = (ProductGroup) obj;
        EqualsBuilder builder = new EqualsBuilder();
        builder.append( getId(), other.getId() );
        return builder.isEquals();
    }
}
