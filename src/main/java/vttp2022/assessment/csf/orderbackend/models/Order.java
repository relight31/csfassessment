package vttp2022.assessment.csf.orderbackend.models;

import java.util.LinkedList;
import java.util.List;

import org.springframework.jdbc.support.rowset.SqlRowSet;

// IMPORTANT: You can add to this class, but you cannot delete its original content

public class Order {

	private Integer orderId;
	private String name;
	private String email;
	private Integer size;
	private String sauce;
	private boolean thickCrust;
	private List<String> toppings = new LinkedList<>();
	private String comments;

	public void setOrderId(Integer orderId) {
		this.orderId = orderId;
	}

	public Integer getOrderId() {
		return this.orderId;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getName() {
		return this.name;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getEmail() {
		return this.email;
	}

	public void setSize(Integer size) {
		this.size = size;
	}

	public Integer getSize() {
		return this.size;
	}

	public void setSauce(String sauce) {
		this.sauce = sauce;
	}

	public String getSauce() {
		return this.sauce;
	}

	public void setThickCrust(Boolean thickCrust) {
		this.thickCrust = thickCrust;
	}

	public Boolean isThickCrust() {
		return this.thickCrust;
	}

	public void setToppings(List<String> toppings) {
		this.toppings = toppings;
	}

	public List<String> getToppings() {
		return this.toppings;
	}

	public void addTopping(String topping) {
		this.toppings.add(topping);
	}

	public void setComments(String comments) {
		this.comments = comments;
	}

	public String getComments() {
		return this.comments;
	}

	public String parseToppingsOut() {
		return String.join(",", toppings);
	}

	public void parseToppingsIn(String toppings) {
		String[] toppingsArray = toppings.split(",");
		for (String item : toppingsArray) {
			this.toppings.add(item);
		}
	}

	public static Order rowsetToOrder(SqlRowSet rowSet) {
		Order order = new Order();
		order.setOrderId(rowSet.getInt("order_id"));
		order.setName(rowSet.getString("name"));
		order.setEmail(rowSet.getString("email"));
		order.setSize(rowSet.getInt("pizza_size"));
		order.setThickCrust(order.thickCrustToBool(rowSet.getInt("thick_crust")));
		order.setSauce(rowSet.getString("sauce"));
		order.parseToppingsIn(rowSet.getString("toppings"));
		order.setComments(rowSet.getString("comments"));

		return order;
	}

	public int getThickCrust() {
		if (this.thickCrust) {
			return 1;
		} else {
			return 0;
		}
	}

	public boolean thickCrustToBool(int crust) {
		return crust == 1;
	}
}
