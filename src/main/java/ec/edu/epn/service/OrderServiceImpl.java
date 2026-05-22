package ec.edu.epn.service;

import ec.edu.epn.dto.OrderItemRequest;
import ec.edu.epn.model.Customer;
import ec.edu.epn.model.Order;
import ec.edu.epn.model.OrderStatus;
import ec.edu.epn.repository.CustomerRepository;
import ec.edu.epn.repository.OrderRepository;
import ec.edu.epn.repository.ProductRepository;
import java.util.List;
import org.springframework.stereotype.Service;

@Service
public class OrderServiceImpl implements OrderService {

    private final CustomerRepository customerRepository;
    private final OrderRepository orderRepository;
    private final ProductRepository productRepository;

    public OrderServiceImpl(CustomerRepository customerRepository, OrderRepository orderRepository, ProductRepository productRepository) {
        this.customerRepository = customerRepository;
        this.orderRepository = orderRepository;
        this.productRepository = productRepository;
    }

    @Override
    public Order createOrder(String customerEmail, List<OrderItemRequest> items) {
        Customer customer = customerRepository.findByEmail(customerEmail)
                .orElseThrow(() -> new RuntimeException("Customer not found with email: " + customerEmail));
        throw new RuntimeException("Not implemented");
    }

    @Override
    public List<Order> findOrdersByCustomer(String customerEmail) {
        Customer customer = customerRepository.findByEmail(customerEmail)
                .orElseThrow(() -> new RuntimeException("Customer not found with email: " + customerEmail));
        return orderRepository.findByCustomerId(customer.getId());
    }

    @Override
    public Order updateOrderStatus(Long orderId, OrderStatus newStatus) {
        Order order = orderRepository.findById(orderId)
                .orElseThrow(() -> new RuntimeException("Order not found with id: " + orderId));
        order.setStatus(newStatus);
        return orderRepository.save(order);
    }
}