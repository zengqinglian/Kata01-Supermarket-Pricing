package qinglian.zeng.practice.kata01.supermarketPricing;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;
import java.util.SortedMap;
import java.util.TreeMap;

import qinglian.zeng.practice.kata01.supermarketPricing.product.Product;
import qinglian.zeng.practice.kata01.supermarketPricing.product.ProductGroup;
import qinglian.zeng.practice.kata01.supermarketPricing.specialOffer.DiscountForPieces;
import qinglian.zeng.practice.kata01.supermarketPricing.specialOffer.DiscountForWeight;
import qinglian.zeng.practice.kata01.supermarketPricing.specialOffer.GroupBuyDiscount;

public class PricingManagerImpl implements PricingManager
{

    @Override
    public long checkOut( Map<Product, Number> products ) {
        if( products == null || products.isEmpty() ) {
            return 0;
        }
        long totalPrice = 0;
        Set<ProductGroup> groups = new HashSet<>();

        for( Entry<Product, Number> pair : products.entrySet() ) {
            if( pair.getKey().getOffers().isEmpty()
                    && (pair.getKey().getGroup() == null || pair.getKey().getGroup().getOffers().isEmpty()) ) {
                totalPrice += calcualtePrice( pair );
                pair.setValue( 0 );
            } else if( !pair.getKey().getOffers().isEmpty() ) {
                totalPrice += calculateDiscount( pair );
            } else if( pair.getKey().getGroup() != null ) {
                groups.add( pair.getKey().getGroup() );
            }
        }

        // Group offer
        // TODO: reduce the loop. find out all necessary info from first loop.
        for( ProductGroup group : groups ) {
            List<GroupBuyDiscount> offers = group.getOffers();
            SortedMap<Product, Integer> map = findProductNumberFromGroup( group, products );
            for( GroupBuyDiscount offer : offers ) {
                totalPrice += getPriceForGroupOffer( offer, map, products );
            }
        }

        for( Entry<Product, Number> pair : products.entrySet() ) {
            if( pair.getKey().getType() == Product.SellingType.PIECES && pair.getValue().intValue() > 0 ) {
                totalPrice += calcualtePrice( pair );
                pair.setValue( 0 );
            }
        }

        return totalPrice;
    }

    private long getPriceForGroupOffer( GroupBuyDiscount offer, SortedMap<Product, Integer> map, Map<Product, Number> products ) {
        long total = 0;
        boolean match = true;
        while( match ) {
            int groupProductNumber = 0;
            List<Product> groupList = new ArrayList<>();
            for( Entry<Product, Integer> entry : map.entrySet() ) {
                if( entry.getValue() > 0 ) {
                    groupProductNumber += entry.getValue();
                    groupList.add( entry.getKey() );
                }
                if( groupProductNumber >= offer.getTotalPieces() ) {
                    break;
                }
            }
            if( groupProductNumber < offer.getTotalPieces() ) {
                match = false;
            } else {
                int remain = offer.getTotalPieces();
                for( Product product : groupList ) {
                    int piece = products.get( product ).intValue();
                    if( piece >= remain ) {
                        products.put( product, piece - remain );
                        map.put( product, piece - remain );
                        break;
                    } else {
                        products.put( product, 0 );
                        map.put( product, 0 );
                        remain = remain - piece;
                    }
                }
                total += offer.getTotalPrice();
            }

        }
        return total;
    }

    // consider highest price first
    private SortedMap<Product, Integer> findProductNumberFromGroup( ProductGroup group, Map<Product, Number> products ) {
        SortedMap<Product, Integer> map = new TreeMap<>( new Comparator<Product>() {

            @Override
            public int compare( Product o1, Product o2 ) {
                if( o1.getUnitPrice() - o2.getUnitPrice() > 0 ) {
                    return -1;
                } else if( o1.getUnitPrice() - o2.getUnitPrice() < 0 ) {
                    return 1;
                } else {
                    return 0;
                }
            }

        } );
        for( Product product : group.getProducts() ) {
            if( products.get( product ).intValue() > 0 ) {
                map.put( product, products.get( product ).intValue() );
            }
        }
        return map;
    }

    private long calcualtePrice( Entry<Product, Number> pair ) {
        if( pair.getKey().getType() == Product.SellingType.PIECES ) {

            return pair.getKey().getUnitPrice() * pair.getValue().intValue();
        }

        if( pair.getKey().getType() == Product.SellingType.WEIGHT ) {
            return Math.round( pair.getKey().getUnitPrice() * pair.getValue().doubleValue() ); // round to closest
        }

        return 0;
    }

    private long calculateDiscount( Entry<Product, Number> pair ) {
        int total = 0;
        if( pair.getKey().getType() == Product.SellingType.PIECES ) {
            List<DiscountForPieces> offers = pair.getKey().getOffers();
            for( DiscountForPieces offer : offers ) {
                while( pair.getValue().intValue() >= offer.getType().getReducePieces() ) {
                    pair.setValue( pair.getValue().intValue() - offer.getType().getReducePieces() );
                    total += offer.getType().getTotalPrice( pair.getKey().getUnitPrice() );
                }
            }
        } else if( pair.getKey().getType() == Product.SellingType.WEIGHT ) {
            List<DiscountForWeight> offers = pair.getKey().getOffers();
            for( DiscountForWeight offer : offers ) {
                if( pair.getValue().doubleValue() >= 0 && pair.getValue().doubleValue() > offer.getType().getMinWeight() ) {
                    total += offer.getType().getTotalPrice( pair.getKey().getUnitPrice(), pair.getValue().doubleValue() );
                    pair.setValue( 0 );
                }
            }
        } else {
            throw new UnsupportedOperationException();
        }

        return total;

    }

}
