package com.murali.order.controller;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.murali.order.entity.Item;
import com.murali.order.entity.Order;
import com.murali.order.external.ProductServiceProxy;
import com.murali.order.repo.OrderRepository;
import com.murali.order.type.customer.CustomerOrderRequest;
import com.murali.order.type.order.CustomerOrderDetails;
import com.murali.order.type.product.ItemDto;
import com.murali.order.type.product.ItemRequest;

import lombok.extern.slf4j.Slf4j;

@RestController
@Slf4j
public class OrderResource {

    @Autowired
    private OrderRepository orderRepository;
    
    @Autowired
    private ProductServiceProxy productServiceProxy;

    @PostMapping("/orders")
    public Order save(@RequestBody CustomerOrderRequest request) {
    	log.debug("Persisting the Order request ["+ request +"]");
        return orderRepository.save(Order
                .builder()
                .customerId(request.getCustomerId())
                .externalReference(request.getExternalReference())
                .items((request.getItems() == null) ? null : toItems(request.getItems())).build());
    }

    @GetMapping("/order")
    public List<CustomerOrderDetails> getCustomerOrders(@RequestParam String customerId) {
    	log.debug("Getting the customer order with customer id ["+ customerId +"]");
        final List<Order> order = orderRepository.findByCustomerId(customerId);
        return order.stream().map(o -> toCustomerOrderDetails(o)).collect(Collectors.toList());
    }

    @GetMapping("/orders/{id}")
    public CustomerOrderDetails getOrders(@PathVariable("id") Long orderId) {
    	log.debug("Getting the customer order with order id ["+ orderId +"]");
        final Order order = orderRepository.findById(orderId).orElse(null);
        if (order == null) {
            return null;
        }
        return toCustomerOrderDetails(order);
    }

    private CustomerOrderDetails toCustomerOrderDetails(Order order) {
        return CustomerOrderDetails.builder()
                .orderId(order.getId())
                .createdDate(order.getCreatedDate())
                .externalReference(order.getExternalReference())
                .items(toItemList(order.getItems()))
                .build();
    }

    private List<ItemDto> toItemList(List<Item> items) {
        return items.stream().map(item -> toItemDto(item)).collect(Collectors.toList());
    }

    private ItemDto toItemDto(Item item) {
        return ItemDto
                .builder()
                .product(productServiceProxy.getProduct(item.getProductId())).build();
    }

    private List<Item> toItems(List<ItemRequest> items) {
        return items.stream().map(itemReq -> Item.builder().productId(itemReq.getProductId())
                .quantity(itemReq.getQuantity()).build())
                .collect(Collectors.toList());
    }
}

