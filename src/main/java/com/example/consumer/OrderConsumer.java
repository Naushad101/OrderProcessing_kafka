package com.example.consumer;

import com.example.entity.Ordered;
import com.example.repository.OrderRepository;
import org.hibernate.query.Order;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

import java.util.Optional;

@Component
public class OrderConsumer {
    public final OrderRepository orderRepository;

    public OrderConsumer(OrderRepository orderRepository) {
        this.orderRepository = orderRepository;
    }
    @KafkaListener(topics = "order", groupId = "order-consumer-group")
    public void listen(Ordered ordered){
        ordered.setStatus("PROCESSED");
        orderRepository.save(ordered);
        System.out.println("Ordered saved successfully in db.");
    }

    @KafkaListener(topics = "orderUp", groupId = "order-consumer-group")
    public void listenUpda(Ordered ordered) throws Exception {
        Optional<Ordered> orderedOptional = orderRepository.findById(ordered.getOrderId());
        if(orderedOptional.isEmpty()){
            throw new Exception("Order is not exists....");
        }
        Ordered ordered1 = orderedOptional.orElseThrow();
        ordered1.setOrderId(ordered.getOrderId());
        ordered1.setPrice(ordered.getPrice());
        ordered1.setQuantity(ordered.getQuantity());
        ordered1.setCustomerId(ordered.getCustomerId());
        ordered1.setProduct(ordered.getProduct());
        ordered1.setStatus("UPDATED");
        orderRepository.save(ordered1);
        System.out.println("Ordered updated successfully in db. "+ordered);
    }

    @KafkaListener(topics = "orderDel",groupId = "order-consumer-group")
    public void listenDel(Ordered ordered) throws Exception {
        if(orderRepository.findById(ordered.getOrderId()).isEmpty()){
            throw new Exception("no order with id "+ordered.getOrderId());
        }
        orderRepository.deleteById(ordered.getOrderId());
        System.out.println("order deleted successfully...."+ordered.getOrderId());
    }
}