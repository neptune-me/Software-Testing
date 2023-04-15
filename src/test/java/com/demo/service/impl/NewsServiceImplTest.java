package com.demo.service.impl;

import com.demo.entity.News;
import com.demo.service.NewsService;
import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.test.context.junit4.SpringRunner;

import java.time.LocalDateTime;
import java.util.List;

import static org.junit.Assert.*;

/**
 * @author yipeng
 */
@RunWith(SpringRunner.class)
@SpringBootTest
public class NewsServiceImplTest {

    @Autowired
    private NewsService newsService;

    @Test
    public void findAll() {
        // 获取到指定的分页数据
        PageRequest pageRequest = PageRequest.of(0, 20);
        Page<News> page = newsService.findAll(pageRequest);
        List<News> content = page.getContent();
        // 判断数据不为空
        Assert.assertNotNull(content);
        // 判断数据的大小不超过分页的大小
        Assert.assertTrue(content.size() <= pageRequest.getPageSize());
    }

    @Test
    public void findById() {
        // 利用创建数据去验证逻辑
        create();
    }

    @Test
    public void create() {

        News news = buildNews();
        int newsId = newsService.create(news);
        Assert.assertTrue(newsId > 0);

        // 查找存在的数据
        News existNews = newsService.findById(newsId);
        Assert.assertNotNull(existNews);

        // 判断获取到的 title 与创建的是否一致
        Assert.assertEquals(existNews.getTitle(), news.getTitle());
        existNews.setTitle("new News Title");
        newsService.update(existNews);
        News updateNews = newsService.findById(newsId);
        Assert.assertEquals(existNews.getTitle(), updateNews.getTitle());
        // 更新之后的 title 与 原先的不一致
        Assert.assertNotEquals(news.getTitle(), updateNews.getTitle());

        // 删除指定 id 的数据
        newsService.delById(newsId);

        // 删除指定 id 的数据后再次查找，数据不存在
        News nonExistNews = newsService.findById(newsId);
        Assert.assertNull(nonExistNews);
    }

    private static News buildNews() {
        News news = new News();
        news.setTitle("News Title");
        news.setContent("News Content");
        news.setTime(LocalDateTime.now());
        return news;
    }

    @Test
    public void delById() {
        // 利用创建数据去验证删除逻辑
        create();
    }

    @Test
    public void update() {
        // 利用创建数据去验证更新逻辑
        create();
    }

}