package edu.eci.arsw.myrestaurant.services;

import edu.eci.arsw.myrestaurant.beans.BillCalculator;
import edu.eci.arsw.myrestaurant.model.Order;
import edu.eci.arsw.myrestaurant.model.ProductType;
import edu.eci.arsw.myrestaurant.model.RestaurantProduct;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

/**
 * Stub implementation of restaurant order services.
 * Manages orders, products, and bill calculations for restaurant tables.
 *
 * @author Jesús Pinzón
 * @version 1.0
 * @since 2025-10-31
 */
@Service
public class RestaurantOrderServicesStub implements RestaurantOrderServices {

    @Autowired
    @Qualifier("basicBillCalculator")
    private BillCalculator calc;

    private static final Map<String, RestaurantProduct> productsMap;
    private static final Map<Integer, Order> tableOrders;

    /**
     * Sets the bill calculator to be used for bill calculations.
     *
     * @param calc The bill calculator implementation
     */
    public void setBillCalculator(BillCalculator calc) {
        this.calc = calc;
    }

    /**
     * Retrieves all orders with their details and total bill.
     * @return Map containing table numbers and their order details with totals
     */
    public Map<Integer, Map<String, Object>> getOrders() {
        Map<Integer, Map<String, Object>> result = new ConcurrentHashMap<>();

        for (Map.Entry<Integer, Order> entry : tableOrders.entrySet()) {
            int tableNumber = entry.getKey();
            Order order = entry.getValue();

            Map<String, Object> orderDetails = new ConcurrentHashMap<>();

            Map<String, Map<String, Object>> productsDetails = new ConcurrentHashMap<>();
            for (String productName : order.getOrderedDishes()) {
                RestaurantProduct product = productsMap.get(productName);
                Map<String, Object> productInfo = new ConcurrentHashMap<>();
                productInfo.put("quantity", order.getDishOrderedAmount(productName));
                productInfo.put("unitPrice", product.getPrice());
                productsDetails.put(productName, productInfo);
            }

            orderDetails.put("orderAmountsMap", order.getOrderAmountsMap());
            orderDetails.put("tableNumber", tableNumber);
            orderDetails.put("total", calc.calculateBill(order, productsMap));

            result.put(tableNumber, orderDetails);
        }

        return result;
    }

    /**
     * Retrieves the order for a specific table.
     *
     * @param tableNumber The table number
     * @return The order for the specified table, or null if no order exists
     */
    @Override
    public Order getTableOrder(int tableNumber) {
        return tableOrders.get(tableNumber);
    }

    /**
     * Gets all available product names.
     * @return Set of product names
     */
    @Override
    public Set<String> getAvailableProductNames() {
        return productsMap.keySet();
    }

    /**
     * Retrieves a product by its name.
     *
     * @param product The product name
     * @return The restaurant product
     * @throws OrderServicesException if the product doesn't exist
     */
    @Override
    public RestaurantProduct getProductByName(String product) throws OrderServicesException {
        if (!productsMap.containsKey(product)) {
            throw new OrderServicesException("Producto no existente:" + product);
        }
        return productsMap.get(product);
    }

    /**
     * Gets all table numbers that have active orders.
     * @return Set of table numbers with orders
     */
    @Override
    public Set<Integer> getTablesWithOrders() {
        return tableOrders.keySet();
    }

    /**
     * Adds a new order to a table.
     *
     * @param o The order to add
     * @throws OrderServicesException if the table already has an open order
     */
    @Override
    public void addNewOrderToTable(Order o) throws OrderServicesException {
        if (tableOrders.containsKey(o.getTableNumber())) {
            throw new OrderServicesException("La mesa tiene una orden abierta. Debe "
                    + "cerrarse la cuenta antes de crear una nueva.:" + o.getTableNumber());
        }
        tableOrders.put(o.getTableNumber(), o);
    }

    /**
     * Releases a table by removing its order.
     *
     * @param tableNumber The table number to release
     * @throws OrderServicesException if the table doesn't exist or is already released
     */
    @Override
    public void releaseTable(int tableNumber) throws OrderServicesException {
        if (!tableOrders.containsKey(tableNumber)) {
            throw new OrderServicesException("Mesa inexistente o ya liberada:" + tableNumber);
        }
        tableOrders.remove(tableNumber);
    }

    /**
     * Calculates the total bill for a table.
     *
     * @param tableNumber The table number
     * @return The total bill amount
     * @throws OrderServicesException if the table doesn't exist or is already released
     */
    @Override
    public int calculateTableBill(int tableNumber) throws OrderServicesException {
        if (!tableOrders.containsKey(tableNumber)) {
            throw new OrderServicesException("Mesa inexistente o ya liberada:" + tableNumber);
        }
        return calc.calculateBill(tableOrders.get(tableNumber), productsMap);
    }

    static {
        productsMap = new ConcurrentHashMap<>();
        tableOrders = new ConcurrentHashMap<>();

        productsMap.put("PIZZA", new RestaurantProduct("PIZZA", 10000, ProductType.DISH));
        productsMap.put("HOTDOG", new RestaurantProduct("HOTDOG", 3000, ProductType.DISH));
        productsMap.put("COKE", new RestaurantProduct("COKE", 1300, ProductType.DRINK));
        productsMap.put("HAMBURGER", new RestaurantProduct("HAMBURGER", 12300, ProductType.DISH));
        productsMap.put("BEER", new RestaurantProduct("BEER", 2500, ProductType.DRINK));

        Order o = new Order(1);
        o.addDish("PIZZA", 3);
        o.addDish("HOTDOG", 1);
        o.addDish("COKE", 4);
        tableOrders.put(1, o);

        Order o2 = new Order(3);
        o2.addDish("HAMBURGER", 2);
        o2.addDish("COKE", 2);
        tableOrders.put(3, o2);
    }
}
