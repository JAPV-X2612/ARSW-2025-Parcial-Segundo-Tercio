package edu.eci.arsw.myrestaurant.restcontrollers;

import edu.eci.arsw.myrestaurant.services.RestaurantOrderServicesStub;
import org.springframework.http.HttpStatus;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

/**
 * REST API Controller for managing restaurant orders.
 * Provides endpoints to retrieve order information.
 *
 * @author Jesús Pinzón
 * @version 1.0
 * @since 2025-10-31
 */
@RestController
@RequestMapping("/orders")
public class OrdersAPIController {

    @Autowired
    private RestaurantOrderServicesStub restaurantOrderServices;

    /**
     * GET endpoint to retrieve all orders with their details and totals.
     *
     * @return ResponseEntity containing the orders map with HTTP 200 status,
     *         or error message with HTTP 500 if an exception occurs
     */
    @RequestMapping(method = RequestMethod.GET)
    public ResponseEntity<?> getOrders() {
        try {
            return new ResponseEntity<>(restaurantOrderServices.getOrders(), HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
}
