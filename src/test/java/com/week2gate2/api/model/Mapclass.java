package com.week2gate2.api.model;

import java.util.List;
import java.util.Map;

public class Mapclass {
    public static final
    Map order_products= Map.of(
            "productId",101,
            "quantity",1,
            "size","UK 7",
            "color","Navy",
            "fulfilment","Home delivery");//map of the  thing that we add in the cart/items

    public static final Map order_address=Map.of(
            "paymentMethod","Credit card",
            "deliverySlot","Tomorrow 9 AM - 12 PM",
            "address","UST Campus, Bengaluru",
            "coupon","",
            "shipping",199,
            "discount",0
    );
    public static final Map order=Map.of("items", List.of(101,107),"currency","INR");
    public static final Map user=Map.of("email","customer@example.com","password","Password@123");
}
