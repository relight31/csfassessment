package vttp2022.assessment.csf.orderbackend.services;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import jakarta.json.JsonArray;
import jakarta.json.JsonObject;
import jakarta.json.JsonValue;
import vttp2022.assessment.csf.orderbackend.models.Order;
import vttp2022.assessment.csf.orderbackend.models.OrderSummary;
import vttp2022.assessment.csf.orderbackend.repositories.OrderRepository;

@Service
public class OrderService {

	@Autowired
	private PricingService priceSvc;

	@Autowired
	private OrderRepository orderRepo;

	// POST /api/order
	// Create a new order by inserting into orders table in pizzafactory database
	// IMPORTANT: Do not change the method's signature
	public boolean createOrder(Order order) {
		return orderRepo.insertNewOrder(order);
	}

	// GET /api/order/<email>/all
	// Get a list of orders for email from orders table in pizzafactory database
	// IMPORTANT: Do not change the method's signature
	public List<OrderSummary> getOrdersByEmail(String email) {
		// Use priceSvc to calculate the total cost of an order
		List<Order> orders = orderRepo.getOrders(email);
		List<OrderSummary> orderSummaries = orders.stream()
				.map((order) -> {
					OrderSummary ordSum = new OrderSummary();
					ordSum.setName(order.getName());
					ordSum.setEmail(order.getEmail());
					ordSum.setOrderId(order.getOrderId());
					ordSum.setAmount(getTotalAmount(order));
					return ordSum;
				})
				.toList();
		return orderSummaries;
	}

	public float getTotalAmount(Order order) {
		float total = 0;
		total += priceSvc.size(order.getSize());
		total += priceSvc.sauce(order.getSauce());
		if (order.isThickCrust()) {
			total += priceSvc.thickCrust();
		} else {
			total += priceSvc.thinCrust();
		}
		for (String topping : order.getToppings()) {
			total += priceSvc.topping(topping);
		}
		return total;
	}

	public Order jsonObjectToOrder(JsonObject object) {
		Order order = new Order();
		order.setName(object.getString("name"));
		order.setEmail(object.getString("email"));
		order.setSize(object.getInt("size"));
		order.setThickCrust(object.getString("base").equals("thick"));
		order.setSauce(object.getString("sauce"));
		JsonArray toppingsArray = object.getJsonArray("toppings");
		for (JsonValue item : toppingsArray) {
			String topping = item.toString();
			order.addTopping(topping);
		}
		order.setComments(object.getString("comments"));

		return order;
	}
}
