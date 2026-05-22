package ec.edu.epn.service;

import ec.edu.epn.dto.OrderItemRequest;
import ec.edu.epn.model.Order;
import ec.edu.epn.model.OrderStatus;
import java.util.List;

public interface OrderService {

    Order createOrder(String customerEmail, List<OrderItemRequest> items);

    List<Order> findOrdersByCustomer(String customerEmail);

    Order updateOrderStatus(Long orderId, OrderStatus newStatus);
}