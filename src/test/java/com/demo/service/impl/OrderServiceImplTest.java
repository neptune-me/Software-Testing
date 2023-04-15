package com.demo.service.impl;

import com.demo.entity.Order;
import com.demo.service.OrderService;
import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import static org.junit.Assert.*;

/**
 * @author yipeng
 */
@RunWith(SpringRunner.class)
@SpringBootTest
public class OrderServiceImplTest {

    @Autowired
    private  OrderService orderService;

    @Test
    public void findById() {
        // 获取到不存在的订单
        Order noExistOrder = orderService.findById(Integer.MIN_VALUE);
        Assert.assertNull(noExistOrder);

        // 获取到存在的订单

    }

    @Test
    public void findDateOrder() {
    }

    @Test
    public void findUserOrder() {
    }

    @Test
    public void updateOrder() {
    }

    @Test
    public void submit() {
    }

    @Test
    public void delOrder() {
    }

    @Test
    public void confirmOrder() {
    }

    @Test
    public void finishOrder() {
    }

    @Test
    public void rejectOrder() {
    }

    @Test
    public void findNoAuditOrder() {
    }

    @Test
    public void findAuditOrder() {
    }

}