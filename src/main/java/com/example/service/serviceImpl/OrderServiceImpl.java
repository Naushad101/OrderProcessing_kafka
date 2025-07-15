package com.example.service.serviceImpl;

import com.example.entity.Ordered;
import com.example.repository.OrderRepository;
import com.example.service.OrderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.support.SendResult;
import org.springframework.stereotype.Service;

import java.util.Optional;
import java.util.concurrent.CompletableFuture;

@Service
public class OrderServiceImpl implements OrderService {

    private final KafkaTemplate<String, Ordered> kafkaTemplate;

    @Autowired
    OrderRepository orderRepository;

    public OrderServiceImpl(KafkaTemplate<String, Ordered> kafkaTemplate) {
        this.kafkaTemplate = kafkaTemplate;
    }

    @Override
    public void saveOrder(Ordered ordered) {
        ordered.setStatus("CREATED");
        String key =ordered.getCustomerId();
        CompletableFuture<SendResult<String, Ordered>> future = kafkaTemplate.send("order", key, ordered);
        logPartition(future, key, "order");
    }

    @Override
    public void updateOrder(Ordered ordered) throws Exception {
        ordered.setStatus("UPDATING..");
        String key = String.valueOf(ordered.getOrderId());
        CompletableFuture<SendResult<String, Ordered>> future = kafkaTemplate.send("orderUp", key, ordered);
        logPartition(future, key, "orderUp");
    }

    @Override
    public void delterOrder(Integer orderId) throws Exception {
        Ordered ordered = new Ordered();
        ordered.setOrderId(orderId);
        String key = String.valueOf(orderId);
        CompletableFuture<SendResult<String, Ordered>> future = kafkaTemplate.send("orderDel", key, ordered);
        logPartition(future, key, "orderDel");
    }

    private void logPartition(CompletableFuture<SendResult<String, Ordered>> future, String key, String topic) {
        future.whenComplete((result, ex) -> {
            if (ex == null) {
                System.out.printf("Message sent to topic: %s, key: %s, partition: %d%n",
                        topic, key, result.getRecordMetadata().partition());
            } else {
                System.err.printf("Failed to send message to topic: %s, key: %s, error: %s%n",
                        topic, key, ex.getMessage());
            }
        });
    }
    @Override
    public Ordered getOrder(Integer orderId) throws Exception {
        Optional<Ordered> optionalOrder = orderRepository.findById(orderId);
        if (optionalOrder.isPresent()) {
            return optionalOrder.get();
        } else {
            throw new Exception("Order not found with id: " + orderId);
        }
    }
}