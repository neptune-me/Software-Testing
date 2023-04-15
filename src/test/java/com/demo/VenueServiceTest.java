//package com.demo;
//
//import com.demo.entity.News;
//import com.demo.entity.User;
//import com.demo.entity.Venue;
//import com.demo.repository.VenueRepository;
//import com.demo.service.VenueService;
//import org.junit.Assert;
//import org.junit.Test;
//import org.junit.runner.RunWith;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.boot.test.context.SpringBootTest;
//import org.springframework.data.domain.Page;
//import org.springframework.data.domain.PageRequest;
//import org.springframework.data.domain.Pageable;
//import org.springframework.data.domain.Sort;
//import org.springframework.test.context.junit4.SpringRunner;
//
//import java.util.List;
//import java.util.Optional;
//
//@RunWith(SpringRunner.class)
//@SpringBootTest
//public class VenueServiceTest {
//    @Autowired
//    private VenueService venueService;
//    @Autowired
//    private VenueRepository venueRepository;
//
//    @Test
//    public void testFindByVenueID() {
//        Venue venue = venueService.findByVenueID(2);
//        Assert.assertNotNull(venue);
//        Assert.assertEquals(1,venue.getVenueID());
//
//
//    }
//
//    @Test
//    public void testFindByVenueName() {
//        Venue venue=venueService.findByVenueName("场馆2");
//        Assert.assertNotNull(venue);
//        Assert.assertEquals("场馆2",venue.getVenueName());
//    }
//
//    @Test
//    public void testFindAll() {
//        Pageable venue_pageable= PageRequest.of(0,1, Sort.by("time").descending());
//        Page<Venue> venue=venueService.findAll(venue_pageable);
//        Assert.assertNotNull(venue);
//    }
//    @Test
//    public void testFindAll2() {
//        // 调用findAll方法
//        List<Venue> venue = venueService.findAll();
//
//        // 判断返回值是否为空
//        Assert.assertNotNull(venue);
//
//        // 判断返回值长度是否大于0
//        Assert.assertTrue(venue.size() > 0);
//    }
//
//
//
//    @Test
//    public void testCreate() {
//        Venue venue = new Venue();
//        venue.setVenueID(123);
//        venue.setVenueName("stadium");
//        venue.setClose_time("17:00");
//        int res = venueService.create(venue);
//        Assert.assertNotEquals(0,res);
//        venueRepository.deleteVenueByVenueID(123);
//    }
//
//    @Test
//    public void testUpdate() {
//        Optional<Venue> OriginalVenue = venueRepository.findById(1);
//        Assert.assertTrue(OriginalVenue.isPresent());
//        // 修改用户ID，密码，昵称
//        Venue venue = new Venue();
//        venue.setVenueID(1);
//        venue.setVenueName("test2");
//        venueService.update(venue);
//        Optional<Venue> actualVenue = venueRepository.findById(1);
//        Assert.assertTrue(actualVenue.isPresent());
//        Assert.assertEquals(actualVenue.get().getVenueID() ,1);
//        Assert.assertEquals(actualVenue.get().getVenueName(), "test2");
//
//        venueService.update(OriginalVenue.get());
//    }
//
//    @Test
//    public void testDelById() {
//        Venue venue = new Venue();
//        venue.setVenueID(222);
//        venue.setVenueName("stadium2");
//
//        venue.setClose_time("17:00");
//        venueRepository.save(venue);
//        int venueID = venueRepository.findVenueByVenueID(222).getVenueID();
//        venueService.delById(venueID);
//        Assert.assertFalse(venueRepository.findById(venueID).isPresent());
//    }
//
//    @Test
//    public void testCountVenueName() {
//        // 计算ID为test的用户数
//        Assert.assertEquals(1, venueService.countVenueName("场馆2"));
//        // 计算ID为not_exist的用户数
//    }
//}
