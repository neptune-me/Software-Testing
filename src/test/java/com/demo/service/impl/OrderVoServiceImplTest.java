package com.demo.service.impl;

import com.demo.entity.Order;
import com.demo.entity.vo.OrderVo;
import com.demo.service.OrderService;
import com.demo.service.OrderVoService;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.Collections;
import java.util.List;

/**
 * @author yipeng
 */
@RunWith(SpringRunner.class)
@SpringBootTest
public class OrderVoServiceImplTest {

    @Autowired
    private OrderVoService orderVoService;

    @Autowired
    private OrderService orderService;

    @Test
    public void returnOrderVoByOrderID() {
        // 根据 orderId 获取到对应的 订单、会场信息
        int orderId = 1;
        OrderVo orderVo = orderVoService.returnOrderVoByOrderID(orderId);
        Assert.assertEquals(orderVo.getOrderID(), orderId);
    }

    @Test
    public void returnVo() {
        int orderId = 1;
        Order order = orderService.findById(orderId);
        // 返回指定 order 列表对应的 orderVo 集合
        List<OrderVo> orderVos = orderVoService.returnVo(Collections.singletonList(order));
        Assert.assertEquals(orderVos.size(), orderId);
        OrderVo orderVo = orderVos.get(0);
        Assert.assertEquals(orderVo.getOrderID(), orderId);
    }

}