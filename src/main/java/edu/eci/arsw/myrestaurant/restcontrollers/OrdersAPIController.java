package edu.eci.arsw.myrestaurant.restcontrollers;

import edu.eci.arsw.myrestaurant.services.RestaurantOrderServicesStub;
import org.springframework.http.HttpStatus;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

/**
 * 
 * @author Jesús Pinzón
 * @since 2025/10/31
 */
@RestController
@RequestMapping("/orders")
public class OrdersAPIController {

    @Autowired
    private RestaurantOrderServicesStub restaurantOrderServices;

    @RequestMapping(method = RequestMethod.GET)
    public ResponseEntity<?> getOrders() {
        try {
            return new ResponseEntity<>(restaurantOrderServices.getOrders(), HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
}
