package com.example.service;

import com.example.entity.Ordered;
import org.hibernate.query.Order;
import org.springframework.stereotype.Service;


public interface OrderService {
    public void saveOrder(Ordered ordered);
    public void updateOrder(Ordered ordered) throws Exception;
    public void deleterOrder(Integer orderId) throws Exception;
    public Ordered getOrder(Integer orderId) throws Exception;
}
