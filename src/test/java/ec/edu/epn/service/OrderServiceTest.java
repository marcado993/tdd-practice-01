package ec.edu.epn.service;

import ec.edu.epn.dto.OrderItemRequest;
import ec.edu.epn.model.Customer;
import ec.edu.epn.model.Order;
import ec.edu.epn.model.OrderStatus;
import ec.edu.epn.model.Product;
import ec.edu.epn.repository.CustomerRepository;
import ec.edu.epn.repository.OrderRepository;
import ec.edu.epn.repository.ProductRepository;
import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class OrderServiceTest {

    @Mock
    private CustomerRepository customerRepository;

    @Mock
    private OrderRepository orderRepository;

    @Mock
    private ProductRepository productRepository;

    private OrderService orderService;

    @BeforeEach
    void setUp() {
        orderService = new OrderServiceImpl(customerRepository, orderRepository, productRepository);
    }

    @Test
    void createOrder_whenCustomerAndStockAreValid_createsPendingOrder() {
        Customer customer = new Customer("Ana", "Lopez", "ana@example.com");
        customer.setId(1L);

        Product product = new Product();
        product.setId(10L);
        product.setSku("SKU-1");
        product.setPrice(new BigDecimal("12.50"));
        product.setStock(5);
        product.setActive(true);

        when(customerRepository.findByEmail("ana@example.com")).thenReturn(Optional.of(customer));
        when(productRepository.findBySku("SKU-1")).thenReturn(Optional.of(product));
        when(orderRepository.save(any(Order.class))).thenAnswer(invocation -> invocation.getArgument(0));

        Order result = orderService.createOrder("ana@example.com", List.of(new OrderItemRequest("SKU-1", 2)));

        assertSame(customer, result.getCustomer());
        assertEquals(OrderStatus.PENDING, result.getStatus());
        assertEquals(new BigDecimal("25.00"), result.getTotal());
        assertEquals(1, result.getItems().size());
        assertEquals(Integer.valueOf(3), product.getStock());
        verify(productRepository).findBySku("SKU-1");
        verify(productRepository).save(product);
        verify(orderRepository).save(any(Order.class));
    }

    @Test
    void createOrder_whenCustomerDoesNotExist_throwsRuntimeException() {
        when(customerRepository.findByEmail("missing@example.com")).thenReturn(Optional.empty());

        assertThrows(RuntimeException.class, () -> orderService.createOrder("missing@example.com", List.of(new OrderItemRequest("SKU-1", 1))));
    }

    @Test
    void findOrdersByCustomer_returnsCustomerOrders() {
        Customer customer = new Customer("Ana", "Lopez", "ana@example.com");
        customer.setId(1L);
        Order order = new Order(customer);
        order.setId(50L);

        when(customerRepository.findByEmail("ana@example.com")).thenReturn(Optional.of(customer));
        when(orderRepository.findByCustomerId(1L)).thenReturn(List.of(order));

        List<Order> result = orderService.findOrdersByCustomer("ana@example.com");

        assertEquals(1, result.size());
        assertSame(order, result.get(0));
    }

    @Test
    void updateOrderStatus_whenOrderExists_updatesStatus() {
        Customer customer = new Customer("Ana", "Lopez", "ana@example.com");
        Order order = new Order(customer);
        order.setId(77L);
        order.setStatus(OrderStatus.PENDING);

        when(orderRepository.findById(77L)).thenReturn(Optional.of(order));
        when(orderRepository.save(any(Order.class))).thenAnswer(invocation -> invocation.getArgument(0));

        Order result = orderService.updateOrderStatus(77L, OrderStatus.COMPLETED);

        assertEquals(OrderStatus.COMPLETED, result.getStatus());
        verify(orderRepository).save(order);
    }
}