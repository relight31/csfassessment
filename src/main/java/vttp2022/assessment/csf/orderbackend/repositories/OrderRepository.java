package vttp2022.assessment.csf.orderbackend.repositories;

import java.util.LinkedList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.rowset.SqlRowSet;
import org.springframework.stereotype.Repository;

import vttp2022.assessment.csf.orderbackend.models.Order;

@Repository
public class OrderRepository {
    @Autowired
    private JdbcTemplate template;

    private static String SQL_GET_ORDERS = "select * from orders where email=?";
    private static String SQL_INSERT_NEW_ORDER = "insert into orders (name, email, pizza_size, thick_crust, sauce, toppings, comments) values (?,?,?,?,?,?,?)";

    public List<Order> getOrders(String email) {
        List<Order> orders = new LinkedList<>();
        SqlRowSet rowSet = template.queryForRowSet(SQL_GET_ORDERS, email);
        while (rowSet.next()) {
            Order order = Order.rowsetToOrder(rowSet);
            orders.add(order);
        }
        return orders;
    }

    public boolean insertNewOrder(Order order) {
        return template.update(
                SQL_INSERT_NEW_ORDER,
                order.getName(),
                order.getEmail(),
                order.getSize(),
                order.getThickCrust(),
                order.getSauce(),
                order.parseToppingsOut(),
                order.getComments()) == 1;
    }
}
