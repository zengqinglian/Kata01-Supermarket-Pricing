package qinglian.zeng.practice.kata01.supermarketPricing.specialOffer;

import java.util.HashSet;
import java.util.Set;

import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.HashCodeBuilder;

import qinglian.zeng.practice.kata01.supermarketPricing.product.ProductGroup;

import com.sun.istack.internal.NotNull;

public class GroupBuyDiscount
{
    // Cover buy any three for x situation.
    // TODO: The lunch box buy any 1*sandwich, 1*coke, 1*crisp is not covered. this need change set to map to specify number for
    // each type. Consdiering create another class to cover this.

    private final int id;// rule id
    private final Set<ProductGroup> productGroups = new HashSet<>();;
    private int totalPieces;
    private long totalPrice;
    private String name;

    public GroupBuyDiscount( int id, @NotNull String name, @NotNull int totalPieces, @NotNull int totalPrice ) {
        this.name = name;
        this.id = id;
        this.totalPieces = totalPieces;
        this.totalPrice = totalPrice;
    }

    public int getTotalPieces() {
        return totalPieces;
    }

    public void setTotalPieces( int totalPieces ) {
        this.totalPieces = totalPieces;
    }

    public long getTotalPrice() {
        return totalPrice;
    }

    public String getName() {
        return name;
    }

    public void setName( String name ) {
        this.name = name;
    }

    public int getId() {
        return id;
    }

    public Set<ProductGroup> getProductGroups() {
        return productGroups;
    }

    public void addProductGroup( ProductGroup productGroup ) {
        productGroups.add( productGroup );
        productGroup.addOffer( this );
    }

    public void setTotalPrice( long totalPrice ) {
        this.totalPrice = totalPrice;
    }

    public void clearProductGroups() {
        for( ProductGroup productGroup : productGroups ) {
            productGroup.removeOffer( this );
        }
        productGroups.clear();
    }

    @Override
    public int hashCode() {
        HashCodeBuilder builder = new HashCodeBuilder();
        builder.append( getId() );
        return builder.hashCode();
    }

    @Override
    public boolean equals( Object obj ) {
        if( !(obj instanceof GroupBuyDiscount) ) {
            return false;
        }
        GroupBuyDiscount other = (GroupBuyDiscount) obj;
        EqualsBuilder builder = new EqualsBuilder();
        builder.append( getId(), other.getId() );
        return builder.isEquals();
    }
}
