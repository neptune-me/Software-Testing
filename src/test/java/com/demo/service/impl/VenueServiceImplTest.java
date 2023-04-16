package com.demo.service.impl;

import com.demo.entity.Venue;
import com.demo.service.VenueService;
import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.List;

import static org.junit.Assert.*;

/**
 * @author yipeng
 */
@RunWith(SpringRunner.class)
@SpringBootTest
public class VenueServiceImplTest {

    @Autowired
    private VenueService venueService;

    @Test
    public void findByVenueID() {
        // 获取到不存在的会场
        Venue venue = venueService.findByVenueID(Integer.MIN_VALUE);
        Assert.assertNull(venue);
        // 获取到存在的会场
        Venue existVenue = venueService.findByVenueID(2);
        Assert.assertNotNull(existVenue);
    }

    @Test
    public void findByVenueName() {
        // 获取到不存在的会场
        Venue venue = venueService.findByVenueName("noExist");
        Assert.assertNull(venue);
        // 获取到存在的会场
        Venue existVenue = venueService.findByVenueName("场馆2");
        Assert.assertEquals(existVenue.getVenueName(), "场馆2");
    }

    @Test
    public void findAll() {
        PageRequest request = PageRequest.of(0, 1);
        // 获取到指定分页的会场信息
        Page<Venue> venuePage = venueService.findAll(request);
        List<Venue> venues = venuePage.getContent();
        Assert.assertTrue(venues.size() <= request.getPageSize());
        Assert.assertTrue(venues.size() > 0);
    }

    @Test
    public void testFindAll() {
        // 获取到所有的会场信息
        List<Venue> venues = venueService.findAll();
        Assert.assertTrue(venues.size() > 0);
    }

    @Test
    public void create() {
        Venue venue = new Venue();
        venue.setVenueName("Venue Name");
        venue.setDescription("Venue Description");
        venue.setPrice(0);
        venue.setPicture("Venue Picture");
        venue.setAddress("Venue Address");
        venue.setOpen_time("09:00");
        venue.setClose_time("12:00");
        // 插入数据
        int venueId = venueService.create(venue);
        Assert.assertTrue(venueId > 0);

        // 插入后的会场数据不为空
        Venue venueExist = venueService.findByVenueID(venueId);
        Assert.assertNotNull(venueExist);

        // 更新数据
        venueExist.setVenueName("Venue Name Update");
        venueService.update(venueExist);
        Venue venueUpdate = venueService.findByVenueID(venueId);
        Assert.assertEquals(venueUpdate.getVenueName(), venueExist.getVenueName());

        // 删除数据
        venueService.delById(venueId);
        Venue venueNotExist = venueService.findByVenueID(venueId);
        Assert.assertNull(venueNotExist);
    }

    @Test
    public void update() {
        create();
    }

    @Test
    public void delById() {
        create();
    }

    @Test
    public void countVenueName() {
        // 不存在的名称个数
        int notExistVenueName = venueService.countVenueName("not exist venueName");
        Assert.assertEquals(0, notExistVenueName);

        // 2222 对应的会场个数
        int count = venueService.countVenueName("2222");
        Assert.assertEquals(1, count);
    }

}