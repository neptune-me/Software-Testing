package com.demo.controller.admin;

import com.demo.entity.Venue;
import com.demo.service.VenueService;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.test.web.servlet.MockMvc;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static org.hamcrest.Matchers.hasSize;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;


@WebMvcTest(AdminVenueController.class)
class AdminVenueControllerTest {
    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private VenueService venueService;

    @Captor
    private ArgumentCaptor<Venue> venueCaptor;

    @Test
    void venue_manage() throws Exception {
        List<Venue> venueList = Arrays.asList(new Venue(),new Venue());

        Page<Venue> venuePage = new PageImpl<>(venueList);

        when(venueService.findAll(any(Pageable.class))).thenReturn(venuePage);

        mockMvc.perform(get("/venue_manage"))
                .andExpect(status().isOk())
                .andExpect(view().name("admin/venue_manage"))
                .andExpect(model().attribute("total", 1));
    }

    @Test
    void editVenue() throws Exception {
        // 模拟要编辑的场馆对象
        Venue venue = new Venue();

        // 模拟场馆服务在调用findByVenueID时返回场馆对象
        when(venueService.findByVenueID(anyInt())).thenReturn(venue);

        // 执行get请求，带有一个场馆ID，断言返回/admin/venue_edit视图，并验证模型中有venue属性
        mockMvc.perform(get("/venue_edit")
                        .param("venueID", "1"))
                .andExpect(status().isOk())
                .andExpect(view().name("/admin/venue_edit"))
                .andExpect(model().attributeExists("venue"));
    }

    @Test
    void venue_add() throws Exception {
        mockMvc.perform(get("/venue_add"))
                .andExpect(view().name("/admin/venue_add"));
    }

    @Test
    void getVenueList() throws Exception {
        // 模拟要返回的场馆列表
        List<Venue> venueList = new ArrayList<>();
        for (int i = 1; i <= 10; i++) {
            Venue venue = new Venue();
            venue.setVenueID(i);
            venueList.add(venue);
        }

        // 模拟场馆服务在调用findAll时返回一个分页对象，包含场馆列表
        Page<Venue> venuePage = new PageImpl<>(venueList);
        when(venueService.findAll(any(Pageable.class))).thenReturn(venuePage);

        // 执行get请求，带有一个页码参数，断言返回JSON格式的场馆列表
        mockMvc.perform(get("/venueList.do")
                        .param("page", "1"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$", hasSize(10)));
    }

    @Test
    void addVenue() throws Exception {
        // 模拟要添加的场馆对象
        Venue venue = new Venue();
        venue.setVenueName("Test Venue");
        venue.setAddress("Test Address");
        venue.setDescription("Test Description");
        venue.setPrice(100);
        venue.setOpen_time("9:00");
        venue.setClose_time("18:00");

        // 模拟一个图片文件
        MockMultipartFile picture = new MockMultipartFile("picture", "test.jpg", "image/jpeg", "test".getBytes());

        // 模拟场馆服务在调用create时返回一个正数或零
        when(venueService.create(any(Venue.class))).thenReturn(1).thenReturn(0);

        // 执行post请求，带有场馆信息和图片文件，断言返回重定向到venue_manage
        mockMvc.perform(multipart("/addVenue.do")
                        .file(picture)
                        .param("venueName", venue.getVenueName())
                        .param("address", venue.getAddress())
                        .param("description", venue.getDescription())
                        .param("price", String.valueOf(venue.getPrice()))
                        .param("open_time", venue.getOpen_time())
                        .param("close_time", venue.getClose_time()))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("venue_manage"));

        // 验证场馆服务被调用了一次，并且场馆对象的信息被设置了
        verify(venueService).create(venueCaptor.capture());
        Venue addedVenue = venueCaptor.getValue();
        assertEquals(venue.getVenueName(), addedVenue.getVenueName());
        assertEquals(venue.getAddress(), addedVenue.getAddress());
        assertEquals(venue.getDescription(), addedVenue.getDescription());
        assertEquals(venue.getPrice(), addedVenue.getPrice());
        assertEquals(venue.getOpen_time(), addedVenue.getOpen_time());
        assertEquals(venue.getClose_time(), addedVenue.getClose_time());

        // 执行post请求，带有场馆信息和空的图片文件，断言返回重定向到venue_add，并且request中有message属性
        mockMvc.perform(multipart("/addVenue.do")
                        .file(new MockMultipartFile("picture", "", "image/jpeg", "".getBytes()))
                        .param("venueName", venue.getVenueName())
                        .param("address", venue.getAddress())
                        .param("description", venue.getDescription())
                        .param("price", String.valueOf(venue.getPrice()))
                        .param("open_time", venue.getOpen_time())
                        .param("close_time", venue.getClose_time()))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("venue_add"));

    }

    @Test
    void modifyVenue() throws Exception {
        // 模拟要修改的场馆对象
        Venue venue = new Venue();
        venue.setVenueID(1);
        venue.setVenueName("Old name");
        venue.setAddress("Old address");
        venue.setDescription("Old description");
        venue.setPrice(100);
        venue.setPicture("old.jpg");
        venue.setOpen_time("9:00");
        venue.setClose_time("18:00");

        // 模拟场馆服务在调用findByVenueID时返回场馆对象
        when(venueService.findByVenueID(anyInt())).thenReturn(venue);

        // 模拟一个新的图片文件
        MockMultipartFile picture = new MockMultipartFile("picture", "new.jpg", "image/jpeg", new byte[]{});

        // 执行post请求，带有新的场馆信息
        mockMvc.perform(multipart("/modifyVenue.do")
                        .file(picture)
                        .param("venueID", "1")
                        .param("venueName", "New name")
                        .param("address", "New address")
                        .param("description", "New description")
                        .param("price", "200")
                        .param("open_time", "10:00")
                        .param("close_time", "20:00"))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("venue_manage"));

        // 验证场馆对象已经更新了新的信息
        verify(venueService).update(venueCaptor.capture());
        Venue updatedVenue = venueCaptor.getValue();
        assertEquals("New name", updatedVenue.getVenueName());
        assertEquals("New address", updatedVenue.getAddress());
        assertEquals("New description", updatedVenue.getDescription());
        assertEquals(200, updatedVenue.getPrice());
        assertEquals("10:00", updatedVenue.getOpen_time());
        assertEquals("20:00", updatedVenue.getClose_time());


    }

    @Test
    void delVenue() throws Exception {
        // 模拟要删除的场馆ID
        int venueID = 1;

        // 执行post请求，带有场馆ID参数
        mockMvc.perform(post("/delVenue.do")
                        .param("venueID", "1"))
                .andExpect(status().isOk())
                .andExpect(content().string("true"));

        // 验证场馆服务已经调用了delById方法
        verify(venueService).delById(venueID);
    }

    @Test
    void checkVenueName() throws Exception {
        // 模拟两个场馆名称，一个已存在，一个不存在
        String existingName = "Old name";
        String newName = "New name";

        // 模拟场馆服务在调用countVenueName时返回不同的结果
        when(venueService.countVenueName(existingName)).thenReturn(1);
        when(venueService.countVenueName(newName)).thenReturn(0);

        // 执行post请求，带有已存在的场馆名称参数
        mockMvc.perform(post("/checkVenueName.do")
                        .param("venueName", existingName))
                .andExpect(status().isOk())
                .andExpect(content().string("false"));

        // 执行post请求，带有不存在的场馆名称参数
        mockMvc.perform(post("/checkVenueName.do")
                        .param("venueName", newName))
                .andExpect(status().isOk())
                .andExpect(content().string("true"));

        // 验证场馆服务已经调用了countVenueName方法
        verify(venueService, times(2)).countVenueName(anyString());
    }
}