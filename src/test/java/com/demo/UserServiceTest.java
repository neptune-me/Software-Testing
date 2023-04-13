package com.demo;

import com.demo.entity.Message;
import com.demo.entity.User;
import com.demo.repository.UserRepository;
import com.demo.service.UserService;
import org.junit.Assert;
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
    private UserRepository userRepository;

    @Test
    public void findByUserID() {
        Assert.assertNotNull(userService.findByUserID("test"));
    }

    @Test
    public void findById() {
        Assert.assertNotNull(userService.findById(1));
    }

    @Test
    public void findByUserID2() {
        Integer pageNum = 1;
        Pageable pageable= PageRequest.of(pageNum - 1, 3);
        Page<User> userPage = userService.findByUserID(pageable);
        Assert.assertEquals(userPage.getNumberOfElements(), 3);
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
        userRepository.deleteUserByUserID("test2");
    }

    @Test
    public void delByID() {
        User user = new User();
        user.setUserID("test2");
        user.setPassword("11");
        user.setUserName("22");
        userRepository.save(user);
        int userID = userRepository.findUserByUserID("test2").getId();
        userService.delByID(userID);
        Assert.assertFalse(userRepository.findById(userID).isPresent());
    }

    @Test
    public void updateUser() {
        // 用来复原id为1的用户信息
        Optional<User> OriginalUser = userRepository.findById(1);
        Assert.assertTrue(OriginalUser.isPresent());
        // 修改用户ID，密码，昵称
        User user = new User();
        user.setId(1);
        user.setUserID("test2");
        user.setPassword("1123");
        user.setUserName("test2");
        userService.updateUser(user);
        Optional<User> actualUser = userRepository.findById(1);
        Assert.assertTrue(actualUser.isPresent());
        Assert.assertEquals(actualUser.get().getUserID(), "test2");
        Assert.assertEquals(actualUser.get().getPassword(), "1123");
        Assert.assertEquals(actualUser.get().getUserName(), "test2");

        userService.updateUser(OriginalUser.get());
    }

    @Test
    public void countUserID() {
        // 计算ID为test的用户数
        Assert.assertEquals(1, userService.countUserID("test"));
        // 计算ID为not_exist的用户数
        Assert.assertEquals(0, userService.countUserID("not_exist"));
    }

}
