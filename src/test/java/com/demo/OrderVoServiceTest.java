//package com.demo;
//import com.demo.entity.Message;
//import com.demo.entity.Order;
//import com.demo.entity.vo.OrderVo;
//import com.demo.repository.OrderRepository;
//import com.demo.service.OrderService;
//import com.demo.service.OrderVoService;
//
//import org.junit.After;
//import org.junit.Assert;
//import org.junit.Before;
//import org.junit.Test;
//import org.junit.runner.RunWith;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.boot.test.context.SpringBootTest;
//import org.springframework.test.context.junit4.SpringRunner;
//
//import java.time.LocalDateTime;
//import java.util.ArrayList;
//import java.util.List;
//
//@RunWith(SpringRunner.class)
//@SpringBootTest
//public class OrderVoServiceTest {
//    @Autowired
//    private OrderVoService orderVoService;
//
//    @Autowired
//    private OrderService orderService;
//
//    @Autowired
//    private OrderRepository orderRepository;
//
//    @Before
//    public void setup(){
//
//    }
//    @Test
//    public void returnOrderVoByOrderID(){
//        int orderID = 1;
//        String userID="test_only";
//        String venueName="ww";
//        LocalDateTime order_time=LocalDateTime.parse("2020-01-02T17:42:02");
//        LocalDateTime start_time=LocalDateTime.parse("2020-01-03T17:42:02");
//
//        Order order = new Order(1,userID,16,2,order_time,start_time,2,600);
////        String venueName = "venue1";
////        LocalDateTime startTime = LocalDateTime.now().plusDays(1);
////        int hours = 2;
////        String userID = "test";
////        orderService.submit(venueName, startTime, hours, userID);
////        Integer actualOrderID=orderService.submit(venueName, startTime, hours, userID);
////        OrderVo actualOrderVo=orderVoService.returnOrderVoByOrderID(actualOrderID);
////        Assert.assertEquals(userID,actualOrderVo.getUserID());
//        orderService.submit(venueName,start_time,2,userID);
//        OrderVo actualOrderVo = orderVoService.returnOrderVoByOrderID(orderID);
//        Assert.assertEquals(orderID, actualOrderVo.getOrderID());
//        Assert.assertEquals(2,actualOrderVo.getState());
//        Assert.assertEquals(start_time,actualOrderVo.getStartTime());
//        Assert.assertEquals(order_time,actualOrderVo.getOrderTime());
//        orderRepository.deleteById(actualOrderVo.getOrderID());
//
//    }
//    @Test
//    public void returnVo(){
//        String userID="test_only2";
//        LocalDateTime order_time=LocalDateTime.parse("2020-02-02T17:42:02");
//        LocalDateTime start_time=LocalDateTime.parse("2020-02-03T17:42:02");
//        List<Order> orderList=new ArrayList<>();
//        List<Integer> orderIdList=new ArrayList<>();
//        List<OrderVo> actualOrderVoList=orderVoService.returnVo(orderList);
//        for (int i = 0; i < 5; i ++) {
////            Order order = new Order(2, userID, 20, 2, order_time,start_time,3,1000);
////            orderService.submit(venueName,start_time,2,userID);
//            OrderVo actualOrderVo=actualOrderVoList.get(i);
//            Assert.assertEquals(orderIdList.get(i),Integer.valueOf(actualOrderVo.getOrderID()));
//            Assert.assertEquals(order_time,actualOrderVo.getOrderTime());
//            Assert.assertEquals(start_time,actualOrderVo.getStartTime());
//            orderRepository.deleteById(actualOrderVo.getOrderID());
//        }
//    }
//    @After
//    public void teardown() {
//
//    }
//}
