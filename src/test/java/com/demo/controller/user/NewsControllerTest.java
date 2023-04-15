package com.demo.controller.user;

import com.demo.entity.News;
import com.demo.service.NewsService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.domain.*;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import org.springframework.web.servlet.ModelAndView;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import static org.mockito.Mockito.when;

@WebMvcTest(NewsController.class)
class NewsControllerTest {

    // 使用@Autowired注解来注入MockMvc对象
    @Autowired
    private MockMvc mockMvc;

    // 使用@MockBean注解来模拟newsService对象
    @MockBean
    private NewsService newsService;

    @Test
    void news() throws Exception {
        // 创建一个News对象作为模拟数据
        News news = new News(1, "标题1", "内容1", LocalDateTime.of(2023, 4, 13, 15, 30));

        // 使用when方法来指定newsService.findById方法的返回值
        when(newsService.findById(1)).thenReturn(news);

        mockMvc.perform(MockMvcRequestBuilders.get("/news").param("newsID", "1"))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.view().name("news"));

    }

    @Test
    void news_list() throws Exception{
        MvcResult result = mockMvc.perform(MockMvcRequestBuilders.get("/news/getNewsList")
                        .param("page", "1"))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andReturn();
        String content = result.getResponse().getContentAsString();
        System.out.println(content);
    }

    @Test
    void testNews_list() throws Exception{
        Pageable news_pageable= PageRequest.of(0,5, Sort.by("time").descending());
        List<News> news = new ArrayList<>();
        News news1 = new News(1, "标题1", "内容1", LocalDateTime.of(2023, 4, 13, 15, 30));
        news.add(news1);
        when(newsService.findAll(news_pageable)).thenReturn(new PageImpl<>(news,news_pageable,1));

        MvcResult result = mockMvc.perform(MockMvcRequestBuilders.get("/news_list"))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.view().name("news_list"))
                .andReturn();
        ModelAndView modelAndView = result.getModelAndView();
        Map<String, Object> model = modelAndView.getModel();
        List<News> newsList = (List<News>) model.get("news_list");
        int total = (int) model.get("total");
        System.out.println(newsList);
        System.out.println(total);
    }
}