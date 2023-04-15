//package com.demo;
//
//
//import com.demo.dao.NewsDao;
//import com.demo.entity.News;
//import com.demo.service.NewsService;
//import com.demo.repository.NewsRepository;
//
//import org.junit.Assert;
//import org.junit.jupiter.api.Test;
//import org.junit.runner.RunWith;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.boot.test.context.SpringBootTest;
//import org.springframework.data.domain.Page;
//import org.springframework.data.domain.PageRequest;
//import org.springframework.data.domain.Pageable;
//import org.springframework.data.domain.Sort;
//import org.springframework.test.annotation.Rollback;
//import org.springframework.test.context.junit4.SpringRunner;
//import org.springframework.transaction.annotation.Transactional;
//import java.time.LocalDateTime;
//
//
//@SpringBootTest
//@RunWith(SpringRunner.class)
//
////@Rollback(value=true)  //value=true的话可以配置事务的回滚,对数据库的增删改都会回滚,便于测试用例的循环利用
//@Transactional
//public class NewsServiceTest {
//    @Autowired
//    public NewsService newsService;
//    //public NewsDao newsDao;
//
//    @Autowired
////    private NewsRepository newsRepository;
//
//    @Test
//    public void test_findById0(){
//        //输入数据库范围外的id（整数）
//        int testId=120;
//
//        News res = newsService.findById(testId);
//        Assert.assertNull(res);
//    }
//    @Test
//    public void test_findById1(){
//        //输入数据库中存在的id
//        int testId=12;
//        News res =newsService.findById(testId);
//        Assert.assertNotNull(res);
//    }
//    @Test
//    public void test_findById2(){
//        // 输入字符，其ASCII码在数据库中不存在对应ID
//        char testId2='a';
//        News res=newsService.findById(testId2);
//        Assert.assertNull(res);
//
//    }
//    @Test
//    public void test_findById3(){
//        // 输入字符，其ASCII码在数据库中存在对应ID（修改了原有数据库的ID）
//        char testId3='a';
//        News res=newsService.findById(testId3);
//        Assert.assertNotNull(res);
//
//    }
////    @Test
////    public void test_findById4(){
////        // 输入字符串、浮点数,直接报错
////        String testId3='a';
////        News findByID=newsService.findById(testId3);
////        System.out.println(findByID);
////    }
//
//
//
//    @Test
//    public void test_findAll(){
//        Pageable news_pageable= PageRequest.of(0,1, Sort.by("time").descending());
//        Page<News> news=newsService.findAll(news_pageable);
//        Assert.assertNotNull(news);
//
//    }
//
//    @Test
//    public void test_create(){
//        String title="hello";
//        String content="";
//        News news= new News();
//        news.setTitle(title);
//        news.setContent(content);
//        news.setTime(LocalDateTime.now());
//        int res = newsService.create(news);
//        System.out.println("newsID="+newsService.create(news));
//        Assert.assertNotEquals(0,res);
//        newsService.update(news);
//    }
//
//    @Test
//    public void test_delById(){
//        News news=new News();
//        news.setNewsID(110);
//        news.setContent("aa");
//        news.setTime(LocalDateTime.now());
//        news.setTitle("hihi");
//        newsRepository.save(news);
//        int newsID=newsRepository.findByNewsID("110").getId();
//        newsService.delById(newsID);
//        //int testId=110;  //120
//        //char testId2='a';
//        Assert.assertFalse(newsRepository.findById(newsID).isPresent());
//    }
//}
//
