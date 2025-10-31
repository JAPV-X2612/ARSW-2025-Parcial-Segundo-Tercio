package edu.eci.arsw.myrestaurant.services;
import edu.eci.arsw.myrestaurant.beans.BillCalculator;

import edu.eci.arsw.myrestaurant.beans.BillCalculator;
import edu.eci.arsw.myrestaurant.model.Order;
import edu.eci.arsw.myrestaurant.model.ProductType;
import edu.eci.arsw.myrestaurant.model.RestaurantProduct;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;


public class RestaurantOrderServicesStub implements RestaurantOrderServices {

    private BillCalculator calc;
    private final Map<Integer, Order> orders = new ConcurrentHashMap<>();
    private final Map<String, RestaurantProduct> products = new ConcurrentHashMap<>();
    private final BasicBillCalculator billCalculator = new BasicBillCalculator();
    private static final Map<String, RestaurantProduct> productsMap;
    private static final Map<Integer, Order> tableOrders;

    public RestaurantOrderServicesStub() {
        // Inicializar productos
        products.put("PIZZA", new RestaurantProduct("PIZZA", 10000));
        products.put("HOTDOG", new RestaurantProduct("HOTDOG", 3000));
        products.put("COKE", new RestaurantProduct("COKE", 1300));
        products.put("HAMBURGER", new RestaurantProduct("HAMBURGER", 12300));

        // Inicializar órdenes
        Order order1 = new Order(1);
        order1.addDish("PIZZA", 3);
        order1.addDish("HOTDOG", 1);
        order1.addDish("COKE", 4);

        Order order3 = new Order(3);
        order3.addDish("HAMBURGER", 2);
        order3.addDish("COKE", 2);

        orders.put(1, order1);
        orders.put(3, order3);
    }

    public void setBillCalculator(BillCalculator calc) {
        this.calc = calc;
    }

    public Map<Integer, Map<String, Object>> getOrders() {
        Map<Integer, Map<String, Object>> result = new ConcurrentHashMap<>();
        for (Map.Entry<Integer, Order> entry : orders.entrySet()) {
            int tableId = entry.getKey();
            Order order = entry.getValue();
            int total = billCalculator.calculateBill(order, products);

            Map<String, Object> orderDetails = new ConcurrentHashMap<>();
            orderDetails.put("products", order.getOrderedDishes());
            orderDetails.put("total", total);

            result.put(tableId, orderDetails);
        }
        return result;
    }

    @Override
    public Order getTableOrder(int tableNumber) {
        if (!tableOrders.containsKey(tableNumber)) {
            return null;
        } else {
            return tableOrders.get(tableNumber);
        }
    }

    @Override
    public Set<String> getAvailableProductNames() {
        return productsMap.keySet();
    }

    @Override
    public RestaurantProduct getProductByName(String product) throws OrderServicesException {
        if (!productsMap.containsKey(product)) {
            throw new OrderServicesException("Producto no existente:" + product);
        } else {
            return productsMap.get(product);
        }
    }

    @Override
    public Set<Integer> getTablesWithOrders() {
        return tableOrders.keySet();
    }

    @Override
    public void addNewOrderToTable(Order o) throws OrderServicesException {
        if (tableOrders.containsKey(o.getTableNumber())) {
            throw new OrderServicesException("La mesa tiene una orden abierta. Debe "
                    + "cerrarse la cuenta antes de crear una nueva.:" + o.getTableNumber());
        } else {
            tableOrders.put(o.getTableNumber(), o);
        }

    }

    @Override
    public void releaseTable(int tableNumber) throws OrderServicesException {
        if (!tableOrders.containsKey(tableNumber)) {
            throw new OrderServicesException("Mesa inexistente o ya liberada:" + tableNumber);
        } else {
            tableOrders.remove(tableNumber);
        }

    }

    @Override
    public int calculateTableBill(int tableNumber) throws OrderServicesException {
        if (!tableOrders.containsKey(tableNumber)) {
            throw new OrderServicesException("Mesa inexistente o ya liberada:" + tableNumber);
        } else {
            return calc.calculateBill(tableOrders.get(tableNumber), productsMap);
        }
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
