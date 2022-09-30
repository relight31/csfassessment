package vttp2022.assessment.csf.orderbackend.controllers;

import java.io.StringReader;
import java.util.List;
import java.util.logging.Logger;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import jakarta.json.Json;
import jakarta.json.JsonArray;
import jakarta.json.JsonObject;
import jakarta.json.JsonReader;
import vttp2022.assessment.csf.orderbackend.models.Order;
import vttp2022.assessment.csf.orderbackend.models.OrderSummary;
import vttp2022.assessment.csf.orderbackend.services.OrderService;

@RestController
@RequestMapping(path = "/api", produces = MediaType.APPLICATION_JSON_VALUE)
public class OrderRestController {
    private Logger logger = Logger.getLogger(OrderRestController.class.getName());

    @Autowired
    private OrderService orderSvc;

    @PostMapping(path = "/order", consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<String> createNewOrder(@RequestBody String payload) {
        JsonReader reader = Json.createReader(new StringReader(payload));
        JsonObject object = reader.readObject();
        Order order = orderSvc.jsonObjectToOrder(object);
        logger.info(OrderRestController.class.getName() + " created Order object, saving to DB");
        if (orderSvc.createOrder(order)) {
            logger.info(OrderRestController.class.getName() + " Saved new order successfully");
            return ResponseEntity.status(HttpStatus.CREATED).build();
        } else {
            return ResponseEntity.badRequest().build();
        }
    }

    @GetMapping(path = "order/{email}/all")
    public ResponseEntity<String> getOrders(@PathVariable String email) {
        List<OrderSummary> orderSummaries = orderSvc.getOrdersByEmail(email);
        JsonArray orderarray = Json.createArrayBuilder().build();
        for (OrderSummary order : orderSummaries) {
            orderarray.add(order.toJsonObject());
        }
        return ResponseEntity.ok(orderarray.toString());
    }
}
