package sample.cafekiosk.spring.api.service.order;


import static sample.cafekiosk.spring.domain.product.ProductSellingStatus.HOLD;
import static sample.cafekiosk.spring.domain.product.ProductSellingStatus.SELLING;
import static sample.cafekiosk.spring.domain.product.ProductSellingStatus.STOP_SELLING;
import static sample.cafekiosk.spring.domain.product.ProductType.HANDMADE;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;
import sample.cafekiosk.spring.IntegrationTestSupport;
import sample.cafekiosk.spring.domain.order.Order;
import sample.cafekiosk.spring.domain.order.OrderStatus;
import sample.cafekiosk.spring.domain.product.Product;
import sample.cafekiosk.spring.domain.product.ProductRepository;
import sample.cafekiosk.spring.domain.product.ProductSellingStatus;
import sample.cafekiosk.spring.domain.product.ProductType;
@Transactional
class OrderRepositoryTest extends IntegrationTestSupport  {

    @Autowired
    private OrderRepository orderRepository;
    @Autowired
    private ProductRepository productRepository;

    @DisplayName("원하는 날짜와 주문 상태로 주문들을 가져온다.")
    @Test
    void findOrdersBy() {
        //given
        LocalDateTime registeredDateTime1 = LocalDateTime.of(2024, 3, 2, 12, 0);
        LocalDateTime registeredDateTime2 = LocalDateTime.of(2024, 3, 9, 12, 0);
        Product product1 = createProduct(SELLING, HANDMADE, "아메리카노", 4000, "001");
        Product product2 = createProduct(HOLD, HANDMADE, "카페라떼", 4500, "002");
        Product product3 = createProduct(STOP_SELLING, HANDMADE, "팥빙수", 7000, "003");
        List<Product> products = List.of(product1, product2, product3);
        productRepository.saveAll(products);

        Order order1 = createOrder(registeredDateTime1, products);
        Order order2 = createOrder(registeredDateTime2, products);
        List<Order> orders = List.of(order1, order2);
        orderRepository.saveAll(orders);

        //when
        LocalDate findDate = LocalDate.of(2024, 3, 2);
        List<Order> findOrders = orderRepository.findOrdersBy(findDate.atStartOfDay(), findDate.atStartOfDay().plusDays(1), OrderStatus.PAYMENT_COMPLETED);

        //then
        Assertions.assertThat(findOrders).hasSize(1);
    }

    private Order createOrder(LocalDateTime registeredDateTime1, List<Product> products) {
        return Order.builder()
                .orderStatus(OrderStatus.PAYMENT_COMPLETED)
                .registeredDateTime(registeredDateTime1)
                .products(products)
                .build();
    }

    private Product createProduct(ProductSellingStatus sellingStatus, ProductType type, String name, int price, String productNumber) {
        return Product.builder()
                .productNumber(productNumber)
                .type(type)
                .sellingStatus(sellingStatus)
                .name(name)
                .price(price)
                .build();
    }

}