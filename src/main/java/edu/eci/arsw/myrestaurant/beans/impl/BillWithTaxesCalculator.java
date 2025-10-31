package edu.eci.arsw.myrestaurant.beans.impl;

import edu.eci.arsw.myrestaurant.beans.BillCalculator;
import edu.eci.arsw.myrestaurant.beans.TaxesCalculator;
import edu.eci.arsw.myrestaurant.beans.impl.colombia.StandardTaxesCalculator;
import edu.eci.arsw.myrestaurant.model.Order;
import edu.eci.arsw.myrestaurant.model.RestaurantProduct;
import java.util.Map;
import org.springframework.stereotype.Service;

/**
 * Bill calculator implementation that calculates the total bill including taxes.
 *
 * @author Jesús Pinzón
 * @version 1.0
 * @since 2025-10-31
 */
@Service
public class BillWithTaxesCalculator implements BillCalculator {

    private final TaxesCalculator taxescalc;

    public BillWithTaxesCalculator() {
        this.taxescalc = new StandardTaxesCalculator();
    }

    /**
     * Calculates the total bill for an order including applicable taxes.
     *
     * @param o The order containing the dishes and their quantities
     * @param productsMap Map containing all available products with their prices
     * @return The total bill amount including taxes
     */
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
