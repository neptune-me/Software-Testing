//package com.demo;
//
//
//import com.demo.entity.Order;
//import com.demo.repository.OrderRepository;
//import com.demo.repository.UserRepository;
//import com.demo.service.OrderService;
//import org.junit.Assert;
//import org.junit.Test;
//import org.junit.runner.RunWith;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.boot.test.context.SpringBootTest;
//import org.springframework.data.domain.Page;
//import org.springframework.data.domain.PageRequest;
//import org.springframework.data.domain.Pageable;
//import org.springframework.test.context.junit4.SpringRunner;
//
//import java.time.LocalDateTime;
//
//@RunWith(SpringRunner.class)
//@SpringBootTest
//public class OrderServiceTest {
//    @Autowired
//    private OrderService orderService;
//    @Autowired
//    private OrderRepository orderRepository;
//
//    @Test
//    public void testFindById() {
//        Order order = orderService.findById(1);
//        Assert.assertNotNull(order);
//        Assert.assertEquals(1, order.getOrderID());
//    }
//
//    @Test
//    public void testFindNoAuditOrder() {
//        Pageable pageable = PageRequest.of(0, 10);
//        Page<Order> orders = orderService.findNoAuditOrder(pageable);
//        Assert.assertNotNull(orders);
//        Assert.assertTrue(orders.getContent().size() > 0);
//    }
//
//    @Test
//    public void testFindAuditOrder() {
//        Assert.assertNotNull(orderService.findAuditOrder());
//    }
//
//    @Test
//    public void testFindDateOrder() {
//        int venueID = 1;
//        LocalDateTime startTime = LocalDateTime.now().minusDays(1);
//        LocalDateTime endTime = LocalDateTime.now().plusDays(1);
//        Assert.assertNotNull(orderService.findDateOrder(venueID, startTime, endTime));
//    }
//
//    @Test
//    public void testFindUserOrder() {
//        String userID = "test";
//        Pageable pageable = PageRequest.of(0, 10);
//        Page<Order> orders = orderService.findUserOrder(userID, pageable);
//        Assert.assertNotNull(orders);
//        Assert.assertTrue(orders.getContent().size() > 0);
//    }
//
//    @Test
//    public void testSubmit() {
//        String venueName = "venue1";
//        LocalDateTime startTime = LocalDateTime.now().plusDays(1);
//        int hours = 2;
//        String userID = "test";
//        orderService.submit(venueName, startTime, hours, userID);
//        Assert.assertNotNull(orderService.findDateOrder(1, startTime, startTime.plusHours(hours)));
//    }
//
//    @Test
//    public void testDelOrder() {
//        //int orderID = 1;
//        Order order = new Order();
//        order.setOrderID(500);
//        order.setOrderTime(LocalDateTime.now());
//        order.setUserID("test");
//        order.setVenueID(16);
//        orderRepository.save(order);
//        int orderID=orderRepository.findByOrderId(500).getOrderID();
//        orderService.delOrder(orderID);
//        Assert.assertFalse(orderRepository.findById(orderID).isPresent());
//    }
//
//    @Test
//    public void testConfirmOrder() {
//        //int orderID = 1;//state=2已审核未完成
//        Order order = new Order();
//        order.setOrderID(1);
//        int orderID=orderRepository.findByOrderId(1).getOrderID();
//        order.setState(1);
//        orderService.confirmOrder(orderID);
//        Assert.assertEquals(2, orderService.findById(orderID).getState());
//    }
//
//    @Test
//    public void testFinishOrder() {
//        Order order = new Order();
//        order.setOrderID(1);
//        int orderID=orderRepository.findByOrderId(1).getOrderID();
//        order.setState(2);
//        orderService.confirmOrder(orderID);
//        orderService.finishOrder(orderID);
//        Assert.assertEquals(3, orderService.findById(orderID).getState());
//    }
//
//    @Test
//    public void testRejectOrder() {
//        Order order = new Order();
//        order.setOrderID(1);
//        int orderID=orderRepository.findByOrderId(1).getOrderID();
//        order.setState(3);
//        orderService.rejectOrder(orderID);
//        Assert.assertEquals(4, orderService.findById(orderID).getState());
//    }
//}
//
