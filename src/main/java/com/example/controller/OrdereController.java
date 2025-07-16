package com.example.controller;

import com.example.entity.Ordered;
import com.example.service.OrderService;
import com.example.service.serviceImpl.OrderServiceImpl;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/order")
public class OrdereController {

    @Autowired
    OrderService orderService;
    Logger logger = org.slf4j.LoggerFactory.getLogger(OrderServiceImpl.class);

    @PostMapping
    public String saveOrdered(@RequestBody Ordered ordered){
        System.out.println("==============================");
        System.out.println(ordered);
        System.out.println("==============================");
        orderService.saveOrder(ordered);
        logger.info("Order saved on main: {}", ordered.getOrderId());
        return "Order successfully created with id: "+ordered.getOrderId();
    }

    @PutMapping
    public String updateOrder(@RequestBody Ordered order) throws Exception {
        orderService.updateOrder(order);
        logger.info("Order updated successfully: {}", order);
        System.out.println("Order updated successfully: "+order);
        return "updated successfully....";
    }

    @DeleteMapping
    public void deleteOrder(@RequestParam Integer orderId) throws Exception {
        orderService.delterOrder(orderId);
        System.out.println("Order deleted successfully: "+orderId);
    }

    @GetMapping("/{orderId}")
    public Ordered getOrder(@PathVariable Integer orderId) throws Exception {   
        Ordered order = orderService.getOrder(orderId);
        System.out.println("Order fetched successfully: "+order);
        System.out.println("==============================");
        logger.info("Order fetched successfully: {}", order);
        System.out.println("==============================");
        return order;
    }
}