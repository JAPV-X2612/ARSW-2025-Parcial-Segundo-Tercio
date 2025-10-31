package edu.eci.arsw.myrestaurant.beans.impl.colombia;

import edu.eci.arsw.myrestaurant.model.RestaurantProduct;
import edu.eci.arsw.myrestaurant.beans.TaxesCalculator;

/**
 * Standard taxes calculator for Colombia.
 * Applies a flat 16% tax rate (IVA) to all products regardless of type.
 *
 * @author Jesús Pinzón
 * @version 1.0
 * @since 2025-10-31
 */
public class StandardTaxesCalculator implements TaxesCalculator {

	/**
	 * Returns the standard tax rate for any product.
	 *
	 * @param p The restaurant product
	 * @return 0.16f representing 16% tax rate
	 */
	@Override
	public float getProductTaxes(RestaurantProduct p) {
		return 0.16f;
	}
}
