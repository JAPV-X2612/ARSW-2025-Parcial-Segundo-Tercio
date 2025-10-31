package edu.eci.arsw.myrestaurant.beans.impl;

import edu.eci.arsw.myrestaurant.beans.BillCalculator;
import edu.eci.arsw.myrestaurant.beans.TaxesCalculator;
import edu.eci.arsw.myrestaurant.model.Order;
import edu.eci.arsw.myrestaurant.model.RestaurantProduct;
import java.util.Map;
import org.springframework.beans.factory.annotation.Autowired;


public class BillWithTaxesCalculator implements BillCalculator {

    @Autowired
    private TaxesCalculator taxescalc;

    @Override
    public int calculateBill(Order o, Map<String, RestaurantProduct> productsMap) {
        int total = 0;

        for (String p : o.getOrderedDishes()) {
            RestaurantProduct rp = productsMap.get(p);
            total += (o.getDishOrderedAmount(p) * (rp.getPrice() * (1 + taxescalc.getProductTaxes(rp))));
        }
        return total;
    }
}
