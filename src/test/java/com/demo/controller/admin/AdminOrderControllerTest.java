package com.demo.controller.admin;

import com.demo.entity.Order;
import com.demo.entity.vo.OrderVo;
import com.demo.service.OrderService;
import com.demo.service.OrderVoService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.domain.*;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.util.Arrays;
import java.util.List;

import static org.hamcrest.Matchers.hasSize;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(AdminOrderController.class)
class AdminOrderControllerTest {
    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private OrderService orderService;

    @MockBean
    private OrderVoService orderVoService;

    @Test
    void reservation_manage() throws Exception {
        // 模拟订单服务返回的订单列表
        Order order = new Order();
        // 根据需要设置订单的属性
        List<Order> orders = Arrays.asList(order);

        // 模拟订单视图服务返回的订单视图列表
        OrderVo orderVo = new OrderVo();
        // 根据需要设置订单视图的属性
        List<OrderVo> orderVos = Arrays.asList(orderVo);

        // 模拟订单服务返回的订单分页
        Pageable order_pageable = PageRequest.of(0, 10, Sort.by("orderTime").descending());
        Page<Order> orderPage = new PageImpl<>(orders);

        // 模拟订单服务在调用时返回订单列表和订单分页
        when(orderService.findAuditOrder()).thenReturn(orders);
        when(orderService.findNoAuditOrder(order_pageable)).thenReturn(orderPage);

        // 模拟订单视图服务在调用时返回订单视图列表
        when(orderVoService.returnVo(orders)).thenReturn(orderVos);

        // 执行get请求并期望一个包含订单视图列表和总页数的模型
        mockMvc.perform(get("/reservation_manage"))
                .andExpect(status().isOk())
                .andExpect(model().attribute("order_list", orderVos))
                .andExpect(model().attribute("total", orderPage.getTotalPages()))
                .andExpect(view().name("admin/reservation_manage"));
    }

    @Test
    void getNoAuditOrder() throws Exception {
        // 模拟订单服务返回的未审核订单分页
        Pageable order_pageable = PageRequest.of(0, 10, Sort.by("orderTime").descending());
        Order order = new Order();
        // 根据需要设置订单的属性
        List<Order> orders = Arrays.asList(order);
        Page<Order> orderPage = new PageImpl<>(orders);

        // 模拟订单视图服务返回的订单视图列表
        OrderVo orderVo = new OrderVo();
        // 根据需要设置订单视图的属性
        List<OrderVo> orderVos = Arrays.asList(orderVo);

        // 模拟订单服务在调用时返回未审核订单分页
        when(orderService.findNoAuditOrder(order_pageable)).thenReturn(orderPage);

        // 模拟订单视图服务在调用时返回订单视图列表
        when(orderVoService.returnVo(orders)).thenReturn(orderVos);

        // 执行get请求并期望一个JSON格式的订单视图列表
        mockMvc.perform(get("/admin/getOrderList.do?page=1"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$", hasSize(1)));
    }

    @Test
    void confirmOrder() throws Exception {
        int orderID = 1;
        mockMvc.perform(post("/passOrder.do")
                .param("orderID", String.valueOf(orderID)))
                .andExpect(content().string("true"));
    }

    @Test
    void rejectOrder() throws Exception {
        int orderID = 1;
        mockMvc.perform(post("/rejectOrder.do")
                        .param("orderID", String.valueOf(orderID)))
                .andExpect(content().string("true"));
    }
}