package com.example.controller;

import com.example.entity.Ordered;
import com.example.service.OrderService;
import com.example.service.serviceImpl.OrderServiceImpl;
import org.hibernate.query.Order;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/order")
public class OrdereController {

    @Autowired
    OrderService orderService;

    @PostMapping
    public String saveOrdered(@RequestBody Ordered ordered){
        System.out.println("==============================");
        System.out.println(ordered);
        System.out.println("==============================");
        orderService.saveOrder(ordered);
        return "Order created successfully with id: "+ordered.getOrderId();
    }

    @PutMapping
    public String updateOrder(@RequestBody Ordered order) throws Exception {
        orderService.updateOrder(order);
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
        return order;
    }
}