package com.example.springsecurityapplication.services;

import com.example.springsecurityapplication.models.OrderRecord;
import com.example.springsecurityapplication.repositories.OrderRecordRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional(readOnly = true)
public class OrderRecordService {
    private final OrderRecordRepository orderRecordRepository;

    public OrderRecordService(OrderRecordRepository orderRecordRepository) {
        this.orderRecordRepository = orderRecordRepository;
    }
    public List<OrderRecord> getAllOrders(){
        return orderRecordRepository.findAll();
    }
    public void updateOrderPerson(int id, OrderRecord orderRecord){
        orderRecord.setId(id);
        orderRecordRepository.save(orderRecord);
    }
    public List<OrderRecord> findByNumberEndingWithIgnoreCase(String endingWith){
        return orderRecordRepository.findByNumberEndingWithIgnoreCase(endingWith);
    }

}
