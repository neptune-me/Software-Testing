package com.demo.controller.user;

import com.demo.entity.Order;
import com.demo.entity.User;
import com.demo.entity.Venue;
import com.demo.entity.vo.OrderVo;
import com.demo.entity.vo.VenueOrder;
import com.demo.service.OrderService;
import com.demo.service.OrderVoService;
import com.demo.service.VenueService;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.domain.*;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.mock.web.MockHttpSession;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.ui.Model;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;


@WebMvcTest(OrderController.class)
class OrderControllerTest {

    @Autowired
    private OrderController orderController;

    @MockBean
    private OrderService orderService;

    @MockBean
    private VenueService venueService;

    @MockBean
    private OrderVoService orderVoService;

    @Autowired
    private MockMvc mockMvc;

    @Test
    void order_manage() {
        //创建一个模拟的请求对象和会话对象
        MockHttpServletRequest request = new MockHttpServletRequest();
        MockHttpSession session = new MockHttpSession();
        request.setSession(session);

        //创建一个模拟的用户对象和订单对象
        User user = new User();
        user.setUserID("1");
        user.setUserName("test");
        user.setPassword("123456");
        Order order = new Order();
        order.setOrderID(1);
        order.setUserID("1");
        order.setState(1);

        //创建一个模拟的分页对象和订单列表
        Pageable order_pageable = PageRequest.of(0,5, Sort.by("orderTime").descending());
        List<Order> orders = new ArrayList<>();
        orders.add(order);
        Page<Order> page = new PageImpl<>(orders, order_pageable, 1);

        //将用户对象添加到会话中
        session.setAttribute("user", user);

        //模拟orderService的findUserOrder方法的返回值
        when(orderService.findUserOrder(user.getUserID(), order_pageable)).thenReturn(page);

        //调用orderController的order_manage方法，并断言返回值为"order_manage"
        Model model = mock(Model.class);
        String result = orderController.order_manage(model, request);
        assertEquals("order_manage", result);

        //断言模型中包含了正确的分页总数
        verify(model).addAttribute("total", 1);
    }

    @Test
    void order_place() {
        //创建一个模拟的请求对象
        MockHttpServletRequest request = new MockHttpServletRequest();

        //创建一个模拟的场馆ID参数
        int venueID = 1;

        //创建一个模拟的场馆对象
        Venue venue = new Venue();
        venue.setVenueID(1);
        venue.setVenueName("Test Venue");
        venue.setAddress("Test Address");
        venue.setPrice(100);

        //模拟venueService的findByVenueID方法的返回值
        when(venueService.findByVenueID(venueID)).thenReturn(venue);

        //调用orderController的order_place方法，并断言返回值为"order_place"
        Model model = mock(Model.class);
        String result = orderController.order_place(model, venueID);
        assertEquals("order_place", result);

        //断言模型中包含了正确的场馆对象
        verify(model).addAttribute("venue", venue);
    }

    @Test
    void testOrder_place() {
        //创建一个模拟的请求对象
        MockHttpServletRequest request = new MockHttpServletRequest();

        //调用orderController的order_place方法，并断言返回值为"order_place"
        Model model = mock(Model.class);
        String result = orderController.order_place(model);
        assertEquals("order_place", result);
    }

    @Test
    void order_list() {
        //创建一个模拟的请求对象和会话对象
        MockHttpServletRequest request = new MockHttpServletRequest();
        MockHttpSession session = new MockHttpSession();
        request.setSession(session);

        //创建一个模拟的用户对象和订单对象
        User user = new User();
        user.setUserID("1");
        user.setUserName("test");
        user.setPassword("123456");
        Order order = new Order();
        order.setOrderID(1);
        order.setUserID("1");
        order.setState(1);

        //创建一个模拟的分页对象和订单列表
        Pageable order_pageable = PageRequest.of(0,5, Sort.by("orderTime").descending());
        List<Order> orders = new ArrayList<>();
        orders.add(order);
        Page<Order> page = new PageImpl<>(orders, order_pageable, 1);

        //创建一个模拟的OrderVo对象和列表
        OrderVo orderVo = new OrderVo();
        orderVo.setOrderID(1);
        orderVo.setVenueName("Test Venue");
        orderVo.setState(2);
        List<OrderVo> orderVos = new ArrayList<>();
        orderVos.add(orderVo);

        //将用户对象添加到会话中
        session.setAttribute("user", user);

        //模拟orderService的findUserOrder方法的返回值
        when(orderService.findUserOrder(user.getUserID(), order_pageable)).thenReturn(page);

        //模拟orderVoService的returnVo方法的返回值
        when(orderVoService.returnVo(page.getContent())).thenReturn(orderVos);

        //调用orderController的order_list方法，并断言返回值为orderVos列表
        List<OrderVo> result = orderController.order_list(1, request);
        assertEquals(orderVos, result);
    }

    @Test
    void addOrder() throws Exception {
        // 创建一个用户对象
        User user = new User(1,"1","test","test123","test@test.com","123456789",0,"");

        //创建一个模拟的HttpSession对象，设置"user"属性
        MockHttpSession session = new MockHttpSession();
        session.setAttribute("user", user);

        String venueName = "Test Venue";
        String date = "";
        String startTime = "2023-04-16 15:00";
        int hours = 2;

        //发送请求并验证结果
        mockMvc.perform(post("/addOrder.do")
                        .param("venueName", venueName)
                        .param("date", date)
                        .param("startTime", startTime)
                        .param("hours", String.valueOf(hours))
                        .session(session))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("order_manage"));
    }

    @Test
    void finishOrder() {
    }

    @Test
    void editOrder() throws Exception {
        Order order = new Order();
        Venue venue = new Venue();

        //模拟orderService和venueService的行为
        Mockito.when(orderService.findById(order.getOrderID())).thenReturn(order);
        Mockito.when(venueService.findByVenueID(order.getVenueID())).thenReturn(venue);

        //构造请求参数
        int orderID = order.getOrderID();

        //发送请求并验证结果
        mockMvc.perform(get("/modifyOrder.do")
                        .param("orderID", String.valueOf(orderID)))
                .andExpect(status().isOk())
                .andExpect(model().attribute("order", order))
                .andExpect(model().attribute("venue", venue))
                .andExpect(view().name("order_edit"));
    }

    @Test
    void modifyOrder() throws Exception {
        Order order = new Order();
        Venue venue = new Venue();
        User user = new User();

        //设置用户的会话属性
        MockHttpSession session = new MockHttpSession();
        session.setAttribute("user", user);

        //构造请求参数
        String venueName = venue.getVenueName();
        String date = "2023-04-20";
        String startTime = "2023-04-20 10:00";
        int hours = 2;
        int orderID = order.getOrderID();

        //发送请求并验证结果
        mockMvc.perform(post("/modifyOrder")
                        .param("venueName", venueName)
                        .param("date", date)
                        .param("startTime", startTime)
                        .param("hours", String.valueOf(hours))
                        .param("orderID", String.valueOf(orderID))
                        .session(session))
                .andExpect(content().string("true"))
                .andExpect(redirectedUrl("order_manage"));
    }

    @Test
    void delOrder() throws Exception {
        //构造请求参数
        int orderID = 1;

        //发送请求并验证结果
        mockMvc.perform(post("/delOrder.do")
                        .param("orderID", String.valueOf(orderID)))
                .andExpect(status().isOk())
                .andExpect(content().string("true"));

        //验证orderService的delOrder方法是否被调用
        Mockito.verify(orderService).delOrder(orderID);
    }

    @Test
    void getOrder() throws Exception {
        Venue venue = new Venue();
        venue.setVenueID(1);
        venue.setVenueName("2222");
        VenueOrder venueOrder = new VenueOrder();
        List<Order> orders = new ArrayList<>();

        when(venueService.findByVenueName(anyString())).thenReturn(venue);

        //构造请求参数
        String venueName = venue.getVenueName();
        String date = "2023-04-20";

        //发送请求并验证结果
        mockMvc.perform(get("/order/getOrderList.do")
                        .param("venueName", venueName)
                        .param("date", date))
                .andExpect(status().isOk());
    }
}