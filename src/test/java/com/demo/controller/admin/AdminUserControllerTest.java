package com.demo.controller.admin;

import com.demo.entity.News;
import com.demo.entity.User;
import com.demo.service.UserService;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.domain.*;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.util.Arrays;
import java.util.List;

import static org.hamcrest.Matchers.hasSize;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(AdminUserController.class)
class AdminUserControllerTest {
    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private UserService userService;

    @Captor
    private ArgumentCaptor<User> userCaptor;

    @Test
    void user_manage() throws Exception {
        List<User> userList = Arrays.asList(new User(),new User());

        Page<User> userPage = new PageImpl<>(userList);

        when(userService.findByUserID(any(Pageable.class))).thenReturn(userPage);

        mockMvc.perform(get("/user_manage"))
                .andExpect(status().isOk())
                .andExpect(view().name("admin/user_manage"))
                .andExpect(model().attribute("total", 1));
    }

    @Test
    void user_add() throws Exception {
        mockMvc.perform(get("/user_add"))
                .andExpect(view().name("admin/user_add"));
    }

    @Test
    void userList() throws Exception {
        // 模拟用户服务返回的用户分页
        Pageable user_pageable = PageRequest.of(0, 10, Sort.by("id").ascending());
        User user = new User();
        // 根据需要设置用户的属性
        List<User> users = Arrays.asList(user);
        Page<User> userPage = new PageImpl<>(users);

        // 模拟用户服务在调用时返回用户分页
        when(userService.findByUserID(user_pageable)).thenReturn(userPage);

        // 执行get请求并期望一个JSON格式的用户列表
        mockMvc.perform(get("/userList.do?page=1"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$", hasSize(1)));
    }

    @Test
    void user_edit() throws Exception {
        // 模拟用户服务返回的用户对象
        User user = new User();
        // 根据需要设置用户的属性
        int id = 1;
        user.setId(id);

        // 模拟用户服务在调用时返回用户对象
        when(userService.findById(id)).thenReturn(user);

        // 执行get请求并期望一个包含用户对象的模型
        mockMvc.perform(get("/user_edit")
                        .param("id", String.valueOf(id)))
                .andExpect(status().isOk())
                .andExpect(model().attribute("user", user))
                .andExpect(view().name("admin/user_edit"));
    }

    @Test
    void modifyUser() throws Exception {
        // 模拟要修改的用户对象
        User user = new User();
        user.setUserID("oldID");
        user.setUserName("oldName");
        user.setPassword("oldPassword");
        user.setEmail("oldEmail");
        user.setPhone("oldPhone");

        // 模拟用户服务在调用findByUserID时返回用户对象
        when(userService.findByUserID(anyString())).thenReturn(user);

        // 执行post请求，带有新的用户信息
        mockMvc.perform(post("/modifyUser.do")
                        .param("userID", "newID")
                        .param("oldUserID", "oldID")
                        .param("userName", "newName")
                        .param("password", "newPassword")
                        .param("email", "newEmail")
                        .param("phone", "newPhone"))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("user_manage"));

        // 验证用户对象已经更新了新的信息
        verify(userService).updateUser(userCaptor.capture());
        User updatedUser = userCaptor.getValue();
        assertEquals("newID", updatedUser.getUserID());
        assertEquals("newName", updatedUser.getUserName());
        assertEquals("newPassword", updatedUser.getPassword());
        assertEquals("newEmail", updatedUser.getEmail());
        assertEquals("newPhone", updatedUser.getPhone());
    }

    @Test
    void addUser() throws Exception {
        // 执行post请求，带有新的用户信息
        mockMvc.perform(post("/addUser.do")
                        .param("userID", "newID")
                        .param("userName", "newName")
                        .param("password", "newPassword")
                        .param("email", "newEmail")
                        .param("phone", "newPhone"))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("user_manage"));

        // 验证用户对象的信息
        verify(userService).create(userCaptor.capture());
        User updatedUser = userCaptor.getValue();
        assertEquals("newID", updatedUser.getUserID());
        assertEquals("newName", updatedUser.getUserName());
        assertEquals("newPassword", updatedUser.getPassword());
        assertEquals("newEmail", updatedUser.getEmail());
        assertEquals("newPhone", updatedUser.getPhone());
    }

    @Test
    void checkUserID() throws Exception {
        // 模拟用户服务在调用countUserID时返回0或1
        when(userService.countUserID(anyString())).thenReturn(0).thenReturn(1);

        // 执行post请求，带有一个不存在的用户ID，断言返回true
        mockMvc.perform(post("/checkUserID.do")
                        .param("userID", "nonexistentID"))
                .andExpect(status().isOk())
                .andExpect(content().string("true"));

        // 执行post请求，带有一个已存在的用户ID，断言返回false
        mockMvc.perform(post("/checkUserID.do")
                        .param("userID", "existentID"))
                .andExpect(status().isOk())
                .andExpect(content().string("false"));

    }

    @Test
    void delUser() throws Exception {
        int id = 1;
        mockMvc.perform(post("/delUser.do")
                .param("id", String.valueOf(id)))
                .andExpect(content().string("true"));
    }
}