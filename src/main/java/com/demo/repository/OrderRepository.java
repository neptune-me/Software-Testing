package com.demo.repository;

import com.demo.entity.News;
import com.demo.entity.Order;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import javax.net.ssl.SSLSession;

@Repository
public interface OrderRepository extends CrudRepository<Order, Integer> {

   Order findByOrderId(int i);
}
