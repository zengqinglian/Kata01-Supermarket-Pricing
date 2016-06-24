package qinglian.zeng.practice.kata01.supermarketPricing;

import static org.junit.Assert.assertEquals;

import java.util.HashMap;
import java.util.Map;

import org.junit.Before;
import org.junit.Test;

import qinglian.zeng.practice.kata01.supermarketPricing.product.Product;
import qinglian.zeng.practice.kata01.supermarketPricing.product.Product.SellingType;
import qinglian.zeng.practice.kata01.supermarketPricing.product.ProductGroup;
import qinglian.zeng.practice.kata01.supermarketPricing.specialOffer.DiscountForPieces;
import qinglian.zeng.practice.kata01.supermarketPricing.specialOffer.DiscountForWeight;
import qinglian.zeng.practice.kata01.supermarketPricing.specialOffer.GroupBuyDiscount;

public class PricingManagerImplTest
{
    private static final int PACK_APPLE_ID = 1;
    private static final String PACK_APPLE_NAME = "Packed Apple";
    private static final long PACK_APPLE_UNIT_PRICE = 150;

    private static final int LOOSE_APPLE_ID = 2;
    private static final String LOOSE_APPLE_NAME = "Loose Apple";
    private static final long LOOSE_APPLE_UNIT_PRICE = 100;

    private static final int WHOLE_CHICKEN_ID = 3;
    private static final String WHOLE_CHICKEN_NAME = "Whole chicken";
    private static final long WHOLE_CHICKEN_UNIT_PRICE = 450;

    private static final int SANDWICH_A_ID = 4;
    private static final String SANDWICH_A_NAME = "Sandwich A";
    private static final long SANDWICH_A_UNIT_PRICE = 250;

    private static final int SANDWICH_B_ID = 5;
    private static final String SANDWICH_B_NAME = "Sandwich B";
    private static final long SANDWICH_B_PRICE = 270;

    private static final int SANDWICH_C_ID = 6;
    private static final String SANDWICH_C_NAME = "Sandwich C";
    private static final long SANDWICH_C_PRICE = 280;

    private static final int ANYTHREE_DEAL_ID = 1;
    private static final String ANYTHREE_DEAL_NAME = "Any Three for x pounds group";

    private static final int BUYONEGETONEFREE_DEAL_ID = 1;
    private static final DiscountForPieces.DiscountType BUYONEGETONEFREE_TYPE = DiscountForPieces.DiscountType.BUGONEGETONEFREE;

    private static final int THREEFORTWO_DEAL_ID = 2;
    private static final DiscountForPieces.DiscountType THREEFORTWO_TYPE = DiscountForPieces.DiscountType.THREEFORTWO;

    private static final int HALFPRICE_PIECE_DEAL_ID = 3;
    private static final DiscountForPieces.DiscountType HALFPRICE_PIECE_TYPE = DiscountForPieces.DiscountType.HALFPRICE;

    private static final int BUYONEGETONEHALF_DEAL_ID = 4;
    private static final DiscountForPieces.DiscountType BUYONEGETONEHALF_TYPE = DiscountForPieces.DiscountType.BUYONEGETONEHALFPRICE;

    private static final int HALFPRICE_WEIGHT_DEAL_ID = 1;
    private static final DiscountForWeight.DiscountType HALFPRICE_WEIGHT_TYPE = DiscountForWeight.DiscountType.HALFPRICE;

    private static final int ANYTHREESANDWICH_OFFER_ID = 1;
    private static final String ANYTHREESANDWICH_OFFER_NAME = "Any three sandwich for 6 pounds";
    private static final int ANYTHREESANDWICH_OFFER_TOTAL_PIECE = 3;
    private static final int ANYTHREESANDWICH_OFFER_TOTAL_PRICE = 600;

    private static final SellingType PIECE = SellingType.PIECES;
    private static final SellingType WEIGHT = SellingType.WEIGHT;

    private PricingManagerImpl pm = new PricingManagerImpl();

    private Product<DiscountForPieces> packedApple;
    private Product<DiscountForWeight> looseApple;
    private Product<DiscountForPieces> wholeChicken;
    private Product<DiscountForPieces> sandwichA;
    private Product<DiscountForPieces> sandwichB;
    private Product<DiscountForPieces> sandwichC;
    private ProductGroup anyThreeGroup;
    private DiscountForPieces buyOneGetOneFree;
    private DiscountForPieces threeForTwo;
    private DiscountForPieces buyOneGetOneHalfPrice;
    private DiscountForPieces halfPricePiece;
    private DiscountForWeight halfPriceWeight;

    private GroupBuyDiscount sandwichGroupDeal;

    private Map<Product, Number> productsForCheckOut = new HashMap<>();

    @Before
    public void setUp() {
        // products
        packedApple = new Product<DiscountForPieces>( PACK_APPLE_ID, PACK_APPLE_NAME, PACK_APPLE_UNIT_PRICE, PIECE );
        wholeChicken = new Product<DiscountForPieces>( WHOLE_CHICKEN_ID, WHOLE_CHICKEN_NAME, WHOLE_CHICKEN_UNIT_PRICE, PIECE );
        sandwichA = new Product<DiscountForPieces>( SANDWICH_A_ID, SANDWICH_A_NAME, SANDWICH_A_UNIT_PRICE, PIECE );
        sandwichB = new Product<DiscountForPieces>( SANDWICH_B_ID, SANDWICH_B_NAME, SANDWICH_B_PRICE, PIECE );
        sandwichC = new Product<DiscountForPieces>( SANDWICH_C_ID, SANDWICH_C_NAME, SANDWICH_C_PRICE, PIECE );

        looseApple = new Product<DiscountForWeight>( LOOSE_APPLE_ID, LOOSE_APPLE_NAME, LOOSE_APPLE_UNIT_PRICE, WEIGHT );
        // product group
        anyThreeGroup = new ProductGroup( ANYTHREE_DEAL_ID, ANYTHREE_DEAL_NAME );

        anyThreeGroup.addProducts( sandwichA );
        anyThreeGroup.addProducts( sandwichB );
        anyThreeGroup.addProducts( sandwichC );

        // discount
        buyOneGetOneFree = new DiscountForPieces( BUYONEGETONEFREE_DEAL_ID, BUYONEGETONEFREE_TYPE );
        threeForTwo = new DiscountForPieces( THREEFORTWO_DEAL_ID, THREEFORTWO_TYPE );
        buyOneGetOneHalfPrice = new DiscountForPieces( BUYONEGETONEHALF_DEAL_ID, BUYONEGETONEHALF_TYPE );
        halfPricePiece = new DiscountForPieces( HALFPRICE_PIECE_DEAL_ID, HALFPRICE_PIECE_TYPE );

        halfPriceWeight = new DiscountForWeight( HALFPRICE_WEIGHT_DEAL_ID, HALFPRICE_WEIGHT_TYPE );

        // group deal
        sandwichGroupDeal = new GroupBuyDiscount( ANYTHREESANDWICH_OFFER_ID, ANYTHREESANDWICH_OFFER_NAME,
                ANYTHREESANDWICH_OFFER_TOTAL_PIECE, ANYTHREESANDWICH_OFFER_TOTAL_PRICE );

        initlial();
    }

    @Test
    public void calculatePriceForEmptyBucket() {
        Map<Product, Number> emptyBuckets = new HashMap<>();
        assertEquals( 0, pm.checkOut( emptyBuckets ) );

        assertEquals( 0, pm.checkOut( null ) );
    }

    @Test
    public void calculatePriceNoDiscount() {
        assertEquals( 1650, pm.checkOut( productsForCheckOut ) );
    }

    @Test
    public void calculatePriceAgainstEachDiscountRule() {
        // buy one get one free
        packedApple.cleanOffers();
        productsForCheckOut.put( packedApple, 3 );
        packedApple.addOffer( buyOneGetOneFree );
        assertEquals( 1800, pm.checkOut( productsForCheckOut ) );

        // three for two
        packedApple.cleanOffers();
        initlial();
        productsForCheckOut.put( packedApple, 4 );
        packedApple.addOffer( threeForTwo );
        assertEquals( 1950, pm.checkOut( productsForCheckOut ) );

        // buy one get one half price
        packedApple.cleanOffers();
        initlial();
        productsForCheckOut.put( packedApple, 3 );
        packedApple.addOffer( buyOneGetOneHalfPrice );
        assertEquals( 1875, pm.checkOut( productsForCheckOut ) );

        // half price piece
        packedApple.cleanOffers();
        initlial();
        productsForCheckOut.put( packedApple, 3 );
        packedApple.addOffer( halfPricePiece );
        assertEquals( 1725, pm.checkOut( productsForCheckOut ) );

        // half price weight
        packedApple.cleanOffers();
        initlial();
        productsForCheckOut.put( packedApple, 1 );
        looseApple.cleanOffers();
        looseApple.addOffer( halfPriceWeight );
        assertEquals( 1525, pm.checkOut( productsForCheckOut ) );

    }

    @Test
    public void calculatePriceWithGroupOffer() {
        anyThreeGroup.addOffer( sandwichGroupDeal );
        assertEquals( 1450, pm.checkOut( productsForCheckOut ) );

        // test higher price product will be consider first
        initlial();
        productsForCheckOut.put( sandwichC, 2 );
        // 150*1+100*2.5+450+600+250 - sandwich a will be the one left after group offer
        assertEquals( 1700, pm.checkOut( productsForCheckOut ) );

    }

    @Test
    public void testDealCombinations() {
        // group offer and buy one get one half
        anyThreeGroup.addOffer( sandwichGroupDeal );
        productsForCheckOut.put( sandwichA, 3 );
        sandwichA.addOffer( buyOneGetOneHalfPrice );

        // buy one get one half price first then group offer
        // 150*1+100*2.5+450+250+250/2 +600
        assertEquals( 1825, pm.checkOut( productsForCheckOut ) );
    }

    private void initlial() {
        productsForCheckOut.put( packedApple, 1 );
        productsForCheckOut.put( wholeChicken, 1 );
        productsForCheckOut.put( sandwichA, 1 );
        productsForCheckOut.put( sandwichB, 1 );
        productsForCheckOut.put( sandwichC, 1 );
        productsForCheckOut.put( looseApple, 2.5 );
    }

}
