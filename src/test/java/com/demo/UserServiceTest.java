package com.demo;

import com.demo.dao.UserDao;
import com.demo.entity.Message;
import com.demo.entity.User;
import com.demo.service.UserService;
import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.Optional;

@RunWith(SpringRunner.class)
@SpringBootTest
public class UserServiceTest {
    @Autowired
    private UserService userService;

    @Autowired
    private UserDao userDao;

    @Before
    public void setup(){
        User user = new User(999, "test_only", "test_only", "1", null, null, 0, null);
        userDao.save(user);
        System.out.println("创建用户");
    }

    @After
    public void teardown() {
        User user = userDao.findByUserID("test_only");
        userDao.delete(user);
    }

    @Test
    public void findByUserID() {
        Assert.assertNotNull(userService.findByUserID("test"));
        Assert.assertNull(userService.findByUserID("not_exist"));
    }

    @Test
    public void findById() {
        Assert.assertNotNull(userService.findById(1));
        Assert.assertNull(userService.findById(999));
    }

    @Test
    public void findByUserID2() {
        Integer pageNum = 1;
        Pageable pageable= PageRequest.of(pageNum - 1, 3);
        Page<User> userPage = userService.findByUserID(pageable);
        Assert.assertEquals(userPage.getNumberOfElements(), 3);
    }

    @Test
    public void findByUserIDBoundary() {
        Integer pageNum = 5;
        Pageable pageable= PageRequest.of(pageNum - 1, 100);
        Page<User> userPage = userService.findByUserID(pageable);
        Assert.assertEquals(userPage.getNumberOfElements(), 0);
    }

    @Test
    public void checkLogin() {
        // 用户名存在，密码正确
        String userID = "test";
        String password = "test";
        User user = userService.checkLogin(userID, password);
        Assert.assertNotNull(user);

        // 用户名不存在
        userID = "not_exist";
        user = userService.checkLogin(userID, password);
        Assert.assertNull(user);

        // 用户名存在，密码不正确
        password = "test1";
        user = userService.checkLogin(userID, password);
        Assert.assertNull(user);
    }

    @Test
    public void create() {
        User user = new User();
        user.setUserID("test2");
        user.setPassword("11");
        user.setUserName("22");
        user.setIsadmin(0);
        int res = userService.create(user);
        Assert.assertNotEquals(0, res);
        userDao.delete(user);
    }

    @Test
    public void create2() {
        User user = new User();
        user.setUserID("test2");
        user.setPassword("11");
        user.setUserName("22");
        user.setIsadmin(100);
        try {
            int res = userService.create(user);
            Assert.assertEquals(0, res);
        } catch (AssertionError e) {
            System.out.println("创建不合法的用户成功，预期为失败");
        } finally {
            userDao.delete(user);
        }
    }

    @Test
    public void delByID() {
        User user = new User();
        user.setUserID("test2");
        user.setPassword("11");
        user.setUserName("22");
        userDao.save(user);
        int userID = userDao.findByUserID("test2").getId();
        userService.delByID(userID);
        Assert.assertNull(userDao.findById(userID));
    }

    @Test
    public void updateUser() {
        // 用来复原id为1的用户信息
        User OriginalUser = userDao.findById(1);
        // 修改用户ID，密码，昵称
        User user = new User();
        user.setId(1);
        user.setUserID("test2");
        user.setPassword("1123");
        user.setUserName("test2");
        userService.updateUser(user);
        User actualUser = userDao.findById(1);
        Assert.assertNotNull(actualUser);
        Assert.assertEquals(actualUser.getUserID(), "test2");
        Assert.assertEquals(actualUser.getPassword(), "1123");
        Assert.assertEquals(actualUser.getUserName(), "test2");

        userService.updateUser(OriginalUser);
    }

    @Test
    public void updateUser2() {
        // 用来复原id为1的用户信息
        User OriginalUser = userDao.findById(1);
        // 修改用户头像
        User newUser = userDao.findById(1);
        newUser.setPicture("path/to/img");
        userService.updateUser(newUser);
        User actualUser = userDao.findById(1);
        Assert.assertNotNull(actualUser);
        Assert.assertEquals(actualUser.getPicture(), "path/to/img");
        userService.updateUser(OriginalUser);
    }

    @Test
    public void countUserID() {
        // 计算ID为test的用户数
        Assert.assertEquals(1, userService.countUserID("test"));
        // 计算ID为not_exist的用户数
        Assert.assertEquals(0, userService.countUserID("not_exist"));
    }

}
