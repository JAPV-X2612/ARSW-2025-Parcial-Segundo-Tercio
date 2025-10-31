package edu.eci.arsw.myrestaurant.beans.impl;

import edu.eci.arsw.myrestaurant.model.Order;
import edu.eci.arsw.myrestaurant.model.RestaurantProduct;
import edu.eci.arsw.myrestaurant.beans.BillCalculator;
import java.util.Map;
import org.springframework.stereotype.Service;

/**
 * Basic bill calculator implementation that calculates the total bill without taxes.
 *
 * @author Jesús Pinzón
 * @version 1.0
 * @since 2025-10-31
 */
@Service
public class BasicBillCalculator implements BillCalculator {

	/**
	 * Calculates the total bill for an order without applying any taxes.
	 *
	 * @param o The order containing the dishes and their quantities
	 * @param productsMap Map containing all available products with their prices
	 * @return The total bill amount without taxes
	 */
	@Override
	public int calculateBill(Order o, Map<String, RestaurantProduct> productsMap) {
		int total = 0;
		for (String p : o.getOrderedDishes()) {
			RestaurantProduct rp = productsMap.get(p);
			total += o.getDishOrderedAmount(p) * rp.getPrice();
		}
		return total;
	}
}
