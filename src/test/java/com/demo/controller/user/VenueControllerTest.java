package com.demo.controller.user;

import com.demo.entity.Venue;
import com.demo.service.VenueService;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.domain.*;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import java.util.ArrayList;
import java.util.List;

import static org.mockito.Mockito.when;

@WebMvcTest(VenueController.class)
class VenueControllerTest {
    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private VenueService venueService;

    @Test
    void toGymPage() throws Exception {
        Venue venue = new Venue(1,"venue","this is description",100,"","address","08:00","18:00");
        when(venueService.findByVenueID(1)).thenReturn(venue);
        mockMvc.perform(MockMvcRequestBuilders.get("/venue").param("venueID", String.valueOf(1)))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.view().name("venue"));
    }

    @Test
    void venue_list() throws Exception {
        // 创建一个Pageable对象，用于作为venueService.findAll方法的参数
        Pageable venue_pageable = PageRequest.of(0, 5, Sort.by("venueID").ascending());

        // 创建一个List对象，用于存储Venue对象，并作为venueService.findAll方法的返回值
        List<Venue> venue_list = new ArrayList<>();
        venue_list.add(new Venue(1, "Gym", "A place to exercise", 100, "gym.jpg", "Beijing", "8:00", "22:00"));
        venue_list.add(new Venue(2, "Pool", "A place to swim", 80, "pool.jpg", "Shanghai", "9:00", "21:00"));
        venue_list.add(new Venue(3, "Stadium", "A place to watch sports", 200, "stadium.jpg", "Guangzhou", "10:00", "20:00"));

        // 创建一个Page对象，用于封装分页信息和Venue对象，并作为venueService.findAll方法的返回值
        Page<Venue> venue_page = new PageImpl<>(venue_list, venue_pageable, 10);

        // 使用Mockito框架来设定当venueService.findAll方法被调用时，返回venue_page对象
        Mockito.when(venueService.findAll(venue_pageable)).thenReturn(venue_page);

        // 使用mockMvc对象来发送一个GET请求，携带参数page=1，并验证返回的JSON数据是否符合预期
        mockMvc.perform(MockMvcRequestBuilders.get("/venuelist/getVenueList").param("page", "1"))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.content[0].venueID").value(1))
                .andExpect(MockMvcResultMatchers.jsonPath("$.content[0].venueName").value("Gym"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.content[0].description").value("A place to exercise"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.content[0].price").value(100))
                .andExpect(MockMvcResultMatchers.jsonPath("$.content[0].picture").value("gym.jpg"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.content[0].address").value("Beijing"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.content[0].open_time").value("8:00"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.content[0].close_time").value("22:00"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.content[1].venueID").value(2))
                .andExpect(MockMvcResultMatchers.jsonPath("$.content[1].venueName").value("Pool"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.content[1].description").value("A place to swim"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.content[1].price").value(80))
                .andExpect(MockMvcResultMatchers.jsonPath("$.content[1].picture").value("pool.jpg"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.content[1].address").value("Shanghai"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.content[1].open_time").value("9:00"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.content[1].close_time").value("21:00"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.content[2].venueID").value(3))
                .andExpect(MockMvcResultMatchers.jsonPath("$.content[2].venueName").value("Stadium"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.content[2].description").value("A place to watch sports"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.content[2].price").value(200))
                .andExpect(MockMvcResultMatchers.jsonPath("$.content[2].picture").value("stadium.jpg"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.content[2].address").value("Guangzhou"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.content[2].open_time").value("10:00"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.content[2].close_time").value("20:00"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.totalPages").value(2));
    }

    @Test
    void testVenue_list() throws Exception {
        // 创建一个Pageable对象，用于作为venueService.findAll方法的参数
        Pageable venue_pageable = PageRequest.of(0, 5, Sort.by("venueID").ascending());

        // 创建一个List对象，用于存储Venue对象，并作为venueService.findAll方法的返回值
        List<Venue> venue_list = new ArrayList<>();
        venue_list.add(new Venue(1,"venue1","this is description",100,"","address1","08:00","18:00"));
        venue_list.add(new Venue(2,"venue2","this is description",200,"","address2","09:00","18:00"));
        venue_list.add(new Venue(3,"venue3","this is description",300,"","address3","10:00","18:00"));

        // 创建一个Page对象，用于封装分页信息和Venue对象，并作为venueService.findAll方法的返回值
        Page<Venue> venue_page = new PageImpl<>(venue_list, venue_pageable, 10);

        // 使用Mockito框架来设定当venueService.findAll方法被调用时，返回venue_page对象
        Mockito.when(venueService.findAll(venue_pageable)).thenReturn(venue_page);

        // 使用mockMvc对象来发送一个GET请求，并验证返回的视图名称和模型属性
        mockMvc.perform(MockMvcRequestBuilders.get("/venue_list"))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.view().name("venue_list"))
                .andExpect(MockMvcResultMatchers.model().attribute("venue_list", venue_list))
                .andExpect(MockMvcResultMatchers.model().attribute("total", 2));

    }
}