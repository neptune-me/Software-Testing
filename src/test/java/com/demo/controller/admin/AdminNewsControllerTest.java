package com.demo.controller.admin;

import com.demo.entity.News;
import com.demo.service.NewsService;
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
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultMatcher;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;

import static org.assertj.core.internal.bytebuddy.matcher.ElementMatchers.is;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(AdminNewsController.class)
class AdminNewsControllerTest {
    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private NewsService newsService;

    @Captor
    private ArgumentCaptor<News> newsCaptor;

    @Test
    void news_manage() throws Exception {

        List<News> newsList = Arrays.asList(new News(),new News());

        Page<News> newsPage = new PageImpl<>(newsList);

        when(newsService.findAll(any(Pageable.class))).thenReturn(newsPage);

        mockMvc.perform(get("/news_manage"))
                .andExpect(status().isOk())
                .andExpect(view().name("admin/news_manage"))
                .andExpect(model().attribute("total", 1));
    }

    @Test
    void news_add() throws Exception {
        mockMvc.perform(get("/news_add"))
                .andExpect(view().name("/admin/news_add"));
    }

    @Test
    void news_edit() throws Exception {
        News news = new News();

        int newsID = 1;

        when(newsService.findById(anyInt())).thenReturn(news);

        mockMvc.perform(get("/news_edit")
                        .param("newsID", String.valueOf(newsID)))
                .andExpect(view().name("/admin/news_edit"));

    }

    @Test
    void newsList() throws Exception {
        News news = new News();

        List<News> newsList = Arrays.asList(news);

        Page<News> newsPage = new PageImpl<>(newsList);

        when(newsService.findAll(any())).thenReturn(newsPage);

        mockMvc.perform(get("/newsList.do?page=1"))
                .andExpect(content().contentType(MediaType.APPLICATION_JSON));
    }

    @Test
    void delNews() throws Exception {
        int newsID = 1;
        mockMvc.perform(post("/delNews.do")
                .param("newsID", String.valueOf(newsID)))
                .andExpect(content().string("true"));
    }

    @Test
    void modifyNews() throws Exception {
        // 模拟要修改的新闻对象
        News news = new News();
//        news.setId(1);
        news.setNewsID(1);
        news.setTitle("Old title");
        news.setContent("Old content");
        news.setTime(LocalDateTime.of(2021, 12, 31, 23, 59));

        // 模拟新闻服务在调用findById时返回新闻对象
        when(newsService.findById(anyInt())).thenReturn(news);

        // 执行post请求，带有新的标题和内容
        mockMvc.perform(post("/modifyNews.do")
                        .param("newsID", "1")
                        .param("title", "New title")
                        .param("content", "New content"))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("news_manage"));

        // 验证新闻对象已经更新了新的标题，内容和时间
        verify(newsService).update(newsCaptor.capture());
        News updatedNews = newsCaptor.getValue();
        assertEquals("New title", updatedNews.getTitle());
        assertEquals("New content", updatedNews.getContent());
        assertTrue(updatedNews.getTime().isAfter(LocalDateTime.of(2021, 12, 31, 23, 59)));
    }

    @Test
    void addNews() throws Exception {
        // 执行post请求，带有新的标题和内容
        mockMvc.perform(post("/addNews.do")
                        .param("title", "New title")
                        .param("content", "New content"))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("news_manage"));

        // 验证新闻对象的标题，内容和时间
        verify(newsService).create(newsCaptor.capture());
        News updatedNews = newsCaptor.getValue();
        assertEquals("New title", updatedNews.getTitle());
        assertEquals("New content", updatedNews.getContent());
        assertTrue(updatedNews.getTime().isAfter(LocalDateTime.of(2021, 12, 31, 23, 59)));
    }
}