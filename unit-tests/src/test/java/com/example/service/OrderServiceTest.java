package com.example.service;

import com.example.models.unit.Order;
import com.example.models.unit.OrderItem;
import com.example.models.unit.OrderStatus;
import com.example.models.unit.Product;
import com.example.repository.OrderRepository;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

@RunWith(MockitoJUnitRunner.class)
public class OrderServiceTest {

    @Mock
    private OrderRepository orderRepository;

    @Mock
    private ProductService productService;

    @InjectMocks
    private OrderService orderService;

    @Test
    public void testCreateOrder_success() {
        // 1. Given (подготовка данных)
        int userId = 1;

        // Создаём товар
        Product product = new Product();
        product.setId(1);
        product.setName("Тестовый товар");
        product.setPrice(new BigDecimal("100.00"));
        product.setStockQuantity(10);

        // Создаём позицию заказа
        OrderItem item = new OrderItem();
        item.setProductId(1);
        item.setQuantity(2);
        item.setPrice(new BigDecimal("100.00"));
        item.calculateTotal(); // должно посчитать 200.00

        List<OrderItem> items = List.of(item);

        // Настраиваем моки
        when(productService.getProductById(1)).thenReturn(Optional.of(product));
        when(productService.isProductInStock(1, 2)).thenReturn(true);

        // При сохранении заказа возвращаем заказ с ID
        Order savedOrder = new Order();
        savedOrder.setId(100);
        when(orderRepository.save(any(Order.class))).thenReturn(savedOrder);

        // 2. When (выполняем тестируемый метод)
        Order result = orderService.createOrder(userId, items);

        // 3. Then (проверяем)
        assertNotNull(result);
        assertEquals(100, result.getId());

        // Проверяем вызовы
        verify(productService).getProductById(1);
        verify(productService).isProductInStock(1, 2);
        verify(productService).decreaseStock(1, 2);
        verify(orderRepository).save(any(Order.class));
    }

    @Test(expected = IllegalStateException.class)
    public void testCreateOrder_productNotInStock() {
        // 1. Given
        int userId = 1;

        OrderItem item = new OrderItem();
        item.setProductId(1);
        item.setQuantity(5);

        List<OrderItem> items = List.of(item);

        // Настраиваем мок: товара нет в нужном количестве
        when(productService.isProductInStock(1, 5)).thenReturn(false);

        // 2. When + Then (ожидаем исключение)
        orderService.createOrder(userId, items);

        // Проверяем, что save НЕ вызывался
        verify(orderRepository, never()).save(any(Order.class));
        verify(productService, never()).decreaseStock(anyInt(), anyInt());
    }

    @Test
    public void testCreateOrder_calculateTotalWithTax() {
        // 1. Given
        int userId = 1;

        // Товар 1: цена 100.00, количество 2 → 200.00
        Product product1 = new Product();
        product1.setId(1);
        product1.setPrice(new BigDecimal("100.00"));
        product1.setStockQuantity(10);

        // Товар 2: цена 50.50, количество 3 → 151.50
        Product product2 = new Product();
        product2.setId(2);
        product2.setPrice(new BigDecimal("50.50"));
        product2.setStockQuantity(10);

        // Позиции заказа
        OrderItem item1 = new OrderItem();
        item1.setProductId(1);
        item1.setQuantity(2);
        item1.setPrice(new BigDecimal("100.00"));

        OrderItem item2 = new OrderItem();
        item2.setProductId(2);
        item2.setQuantity(3);
        item2.setPrice(new BigDecimal("50.50"));

        List<OrderItem> items = List.of(item1, item2);

        // Настраиваем моки
        when(productService.getProductById(1)).thenReturn(Optional.of(product1));
        when(productService.getProductById(2)).thenReturn(Optional.of(product2));
        when(productService.isProductInStock(1, 2)).thenReturn(true);
        when(productService.isProductInStock(2, 3)).thenReturn(true);

        // Захватываем сохранённый заказ для проверки
        when(orderRepository.save(any(Order.class))).thenAnswer(invocation -> {
            Order order = invocation.getArgument(0);
            order.setId(200); // присваиваем ID
            return order;
        });

        // 2. When
        Order result = orderService.createOrder(userId, items);

        // 3. Then
        assertNotNull(result);

        // Ожидаемые суммы
        BigDecimal expectedSubtotal = new BigDecimal("200.00").add(new BigDecimal("151.50")); // 351.50
        BigDecimal expectedTax = expectedSubtotal.multiply(new BigDecimal("0.20")); // 70.30
        BigDecimal expectedTotal = expectedSubtotal.add(expectedTax); // 421.80

        assertEquals(expectedTotal, result.getTotalAmount());

        // Проверяем, что налог посчитан правильно
        BigDecimal actualSubtotal = expectedSubtotal; // мы не храним subtotal в заказе, только total
        BigDecimal calculatedTax = result.getTotalAmount().subtract(actualSubtotal);
        assertEquals(expectedTax, calculatedTax);

        verify(productService, times(2)).getProductById(anyInt());
        verify(productService, times(2)).isProductInStock(anyInt(), anyInt());
        verify(productService).decreaseStock(1, 2);
        verify(productService).decreaseStock(2, 3);
    }

    @Test
    public void testCancelOrder_success() {
        // 1. Given
        int orderId = 100;

        Order order = new Order();
        order.setId(orderId);
        order.setStatus(OrderStatus.NEW);

        when(orderRepository.findById(orderId)).thenReturn(Optional.of(order));
        when(orderRepository.save(any(Order.class))).thenReturn(order);

        // 2. When
        boolean result = orderService.cancelOrder(orderId);

        // 3. Then
        assertTrue(result);
        assertEquals(OrderStatus.CANCELLED, order.getStatus());

        verify(orderRepository).findById(orderId);
        verify(orderRepository).save(order);
    }

    @Test
    public void testCancelOrder_alreadyCompleted() {
        // 1. Given
        int orderId = 101;

        Order order = new Order();
        order.setId(orderId);
        order.setStatus(OrderStatus.COMPLETED); // уже выполнен

        when(orderRepository.findById(orderId)).thenReturn(Optional.of(order));

        // 2. When
        boolean result = orderService.cancelOrder(orderId);

        // 3. Then
        assertFalse(result);
        assertEquals(OrderStatus.COMPLETED, order.getStatus()); // статус не изменился

        verify(orderRepository).findById(orderId);
        verify(orderRepository, never()).save(any(Order.class));
    }

    @Test
    public void testCancelOrder_notFound() {
        // 1. Given
        int orderId = 999;

        when(orderRepository.findById(orderId)).thenReturn(Optional.empty());

        // 2. When
        boolean result = orderService.cancelOrder(orderId);

        // 3. Then
        assertFalse(result);

        verify(orderRepository).findById(orderId);
        verify(orderRepository, never()).save(any(Order.class));
    }
}