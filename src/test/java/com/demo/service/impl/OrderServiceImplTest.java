package com.demo.service.impl;

import com.demo.entity.Order;
import com.demo.service.OrderService;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.test.context.junit4.SpringRunner;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;

import static org.junit.Assert.assertEquals;

/**
 * @author yipeng
 */
@RunWith(SpringRunner.class)
@SpringBootTest
public class OrderServiceImplTest {

    @Autowired
    private OrderService orderService;

    @Test
    public void findById() {
        // 获取到不存在的订单
        Order noExistOrder = orderService.findById(Integer.MIN_VALUE);
        Assert.assertNull(noExistOrder);
        // 获取到存在的订单
        Order order = orderService.findById(1);
        Assert.assertNotNull(order);
    }

    @Test
    public void findDateOrder() {
        // 订单的开始时间在指定的时间之间才会被查找出来
        // 指定的时间不对，查找出来的数据为空
        List<Order> dateOrder = orderService.findDateOrder(2, LocalDateTime.now(), LocalDateTime.now().plusDays(1));
        assertEquals(0, dateOrder.size());

        // 指定正确的时间，查看数据是否存在
        dateOrder = orderService.findDateOrder(2, LocalDateTime.now().plusYears(-4), LocalDateTime.now());
        Assert.assertTrue(dateOrder.size() > 0);
    }

    @Test
    public void findUserOrder() {
        String userId = "test";
        Page<Order> userOrderPage = orderService.findUserOrder(userId, PageRequest.of(0, 10));
        List<Order> orders = userOrderPage.getContent();
        // 查找出来的订单个数不是 0
        Assert.assertTrue(orders.size() > 0);
        // 判断订单的用户都为指定的用户
        for (Order order : orders) {
            Assert.assertEquals(order.getUserID(), userId);
        }
    }

    @Test
    public void updateOrder() {
        Order order = orderService.findById(1);
        // 更新订单的相关信息
        orderService.updateOrder(1, "场馆3", LocalDateTime.now(), 1, "test");
        // 获取到更新后的订单
        Order updateOrder = orderService.findById(1);
        // 判断数据是否更新
        Assert.assertNotEquals(order.getStartTime(), updateOrder.getStartTime());
    }

    @Test
    public void submit() {
        Order order = orderService.submit("场馆2", LocalDateTime.now(), 1, "test");
        // 插入之后自动生成 id
        Assert.assertTrue(order.getOrderID() > 0);
    }

    @Test
    public void delOrder() {
        // 删除前不为空
        Order order = orderService.findById(30);
        Assert.assertNotNull(order);
        // 删除数据
        orderService.delOrder(30);
        // 删除之后为空
        order = orderService.findById(30);
        Assert.assertNull(order);
    }

    @Test
    public void confirmOrder() {
        // 获取到没有审核的订单
        Page<Order> orderPage = orderService.findNoAuditOrder(PageRequest.of(0, 10));
        List<Order> orders = orderPage.getContent();
        if (orders.size() > 0) {
            Order order = orders.get(0);
            // 将为通过的订单转换为已审核
            orderService.confirmOrder(order.getOrderID());
            Order auditOrder = orderService.findById(order.getOrderID());
            Assert.assertEquals(auditOrder.getState(), OrderService.STATE_WAIT);
        }
    }

    @Test
    public void finishOrder() {
        // 将订单的状态置为已完成
        orderService.finishOrder(1);
        Order order = orderService.findById(1);
        Assert.assertEquals(order.getState(), OrderService.STATE_FINISH);
    }

    @Test
    public void rejectOrder() {
        // 将订单的状态置为已拒绝
        orderService.rejectOrder(1);
        Order order = orderService.findById(1);
        Assert.assertEquals(order.getState(), OrderService.STATE_REJECT);
    }

    @Test
    public void findNoAuditOrder() {
        // 参照确认订单接口
        confirmOrder();
    }

    @Test
    public void findAuditOrder() {
        // 获取到审核通过、订单完成的订单
        List<Order> auditOrder = orderService.findAuditOrder();
        List<Integer> auditStates = Arrays.asList(OrderService.STATE_WAIT, OrderService.STATE_FINISH);
        for (Order order : auditOrder) {
            // 获取到的订单的状态一定是审核通过或者是已完成
            Assert.assertTrue(auditStates.contains(order.getState()));
        }
    }

}