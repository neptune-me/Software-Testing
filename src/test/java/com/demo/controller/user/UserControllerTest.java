package com.demo.controller.user;

import com.demo.entity.User;
import com.demo.service.UserService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.mock.web.MockHttpSession;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.request.MockHttpServletRequestBuilder;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.view;

@WebMvcTest(UserController.class)
class UserControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private UserService userService;


    @Test
    void signUp() throws Exception {
        mockMvc.perform(get("/signup"))
                .andExpect(view().name("signup"));
    }

    @Test
    void login() throws Exception {
        mockMvc.perform(get("/login"))
                .andExpect(view().name("login"));
    }

    //编写一个测试方法，用于测试登录成功的情况
    @Test
    public void testLoginSuccess() throws Exception {
        //创建一个模拟的HttpServletRequest对象，设置请求路径，请求方法，请求参数
        MockHttpServletRequestBuilder request = MockMvcRequestBuilders.post("/loginCheck.do")
                .param("userID", "user1")
                .param("password", "123456");

        User user = new User();
        user.setIsadmin(0);
        when(userService.checkLogin("user1","123456")).thenReturn(user);

        //使用MockMvc对象发送请求，并接收响应结果
        MvcResult result = mockMvc.perform(request).andReturn();

        //获取响应内容
        String content = result.getResponse().getContentAsString();

        //断言响应内容是否符合预期
        assertEquals("/index", content);
    }

    @Test
    public void testLoginSuccessAdmin() throws Exception {
        //创建一个模拟的HttpServletRequest对象，设置请求路径，请求方法，请求参数
        MockHttpServletRequestBuilder request = MockMvcRequestBuilders.post("/loginCheck.do")
                .param("userID", "user1")
                .param("password", "123456");

        User user = new User();
        user.setIsadmin(1);
        when(userService.checkLogin("user1","123456")).thenReturn(user);

        //使用MockMvc对象发送请求，并接收响应结果
        MvcResult result = mockMvc.perform(request).andReturn();

        //获取响应内容
        String content = result.getResponse().getContentAsString();

        //断言响应内容是否符合预期
        assertEquals("/admin_index", content);
    }

    //编写一个测试方法，用于测试登录失败的情况
    @Test
    public void testLoginFail() throws Exception {
        //创建一个模拟的HttpServletRequest对象，设置请求路径，请求方法，请求参数
        MockHttpServletRequestBuilder request = MockMvcRequestBuilders.post("/loginCheck.do")
                .param("userID", "user2")
                .param("password", "wrong");

        //使用MockMvc对象发送请求，并接收响应结果
        MvcResult result = mockMvc.perform(request).andReturn();

        //获取响应内容
        String content = result.getResponse().getContentAsString();

        //断言响应内容是否符合预期
        assertEquals("false", content);
    }

    @Test
    public void testRegisterSuccess() throws Exception {
        //创建一个模拟的HttpServletRequest对象，设置请求路径，请求方法，请求参数
        MockHttpServletRequestBuilder request = MockMvcRequestBuilders.post("/register.do")
                .param("userID", "user3")
                .param("userName", "test")
                .param("password", "123456")
                .param("email", "test@test.com")
                .param("phone", "123456789");

        //使用MockMvc对象发送请求，并接收响应结果
        MvcResult result = mockMvc.perform(request).andReturn();

        //获取响应状态码
        int status = result.getResponse().getStatus();

        //断言响应状态码是否符合预期
        assertEquals(302, status); //302表示重定向

        //获取重定向的地址
        String location = result.getResponse().getHeader("Location");

        //断言重定向的地址是否符合预期
        assertEquals("login", location);
    }


    @Test
    public void testLogoutSuccess() throws Exception {
        //创建一个模拟的HttpServletRequest对象，设置请求路径，请求方法
        MockHttpServletRequestBuilder request = get("/logout.do");

        //创建一个模拟的HttpSession对象，设置"user"属性
        MockHttpSession session = new MockHttpSession();
        session.setAttribute("user", new User());

        //将模拟的HttpSession对象添加到模拟的HttpServletRequest对象中
        request.session(session);

        //使用MockMvc对象发送请求，并接收响应结果
        MvcResult result = mockMvc.perform(request).andReturn();

        //获取响应状态码
        int status = result.getResponse().getStatus();

        //断言响应状态码是否符合预期
        assertEquals(302, status); //302表示重定向

        //获取重定向的地址
        String location = result.getResponse().getHeader("Location");

        //断言重定向的地址是否符合预期
        assertEquals("/index", location);

        //断言session中是否已经移除了"user"属性
        assertNull(session.getAttribute("user"));
    }

    @Test
    void testQuitSuccess() throws Exception {
        //创建一个模拟的HttpServletRequest对象，设置请求路径，请求方法
        MockHttpServletRequestBuilder request = get("/quit.do");

        //创建一个模拟的HttpSession对象，设置"user"属性
        MockHttpSession session = new MockHttpSession();
        session.setAttribute("admin", new User());

        //将模拟的HttpSession对象添加到模拟的HttpServletRequest对象中
        request.session(session);

        //使用MockMvc对象发送请求，并接收响应结果
        MvcResult result = mockMvc.perform(request).andReturn();

        //获取响应状态码
        int status = result.getResponse().getStatus();

        //断言响应状态码是否符合预期
        assertEquals(302, status); //302表示重定向

        //获取重定向的地址
        String location = result.getResponse().getHeader("Location");

        //断言重定向的地址是否符合预期
        assertEquals("/index", location);

        //断言session中是否已经移除了"user"属性
        assertNull(session.getAttribute("admin"));
    }

    //编写一个测试方法，用于测试修改个人信息成功的情况
    @Test
    public void testUpdateUserSuccess() throws Exception {
        //创建一个模拟的MultipartFile对象，表示头像文件
        MockMultipartFile picture = new MockMultipartFile("picture", "test.jpg", "image/jpeg", new byte[]{1, 2, 3});

        //使用MockMvcRequestBuilders.multipart()方法创建一个multipart类型的MockHttpServletRequestBuilder对象
        MockHttpServletRequestBuilder requestBuilder = MockMvcRequestBuilders.multipart("/updateUser.do")
                .file(picture) //添加文件
                .param("userName", "test") //添加参数
                .param("userID","user1")
                .param("passwordNew","654321")
                .param("email", "test@test.com")
                .param("phone", "123456789");

        //创建一个模拟的HttpSession对象，设置"user"属性
        MockHttpSession session = new MockHttpSession();
        session.setAttribute("user", new User());

        when(userService.findByUserID("user1")).thenReturn(new User());

        //将模拟的HttpSession对象添加到MockHttpServletRequestBuilder对象中
        requestBuilder.session(session);

        //使用MockMvc对象发送请求，并接收响应结果
        MvcResult result = mockMvc.perform(requestBuilder).andReturn();

        //获取响应状态码
        int status = result.getResponse().getStatus();

        //断言响应状态码是否符合预期
        assertEquals(302, status); //302表示重定向

        //获取重定向的地址
        String location = result.getResponse().getHeader("Location");

        //断言重定向的地址是否符合预期
        assertEquals("user_info", location);

        //获取session中的"user"属性
        User user = (User) session.getAttribute("user");

        //断言session中的"user"属性是否已经更新为修改后的值
        assertEquals("test", user.getUserName());
        assertEquals("654321", user.getPassword());
        assertEquals("test@test.com", user.getEmail());
        assertEquals("123456789", user.getPhone());
    }

    @Test
    void checkPassword() throws Exception {
        User user = new User();
        user.setUserID("user3");
        user.setPassword("password3");
        when(userService.findByUserID("user3")).thenReturn(user);
        MvcResult result = mockMvc.perform(get("/checkPassword.do")
                        .param("userID", user.getUserID())
                        .param("password", user.getPassword()))
                .andExpect(status().isOk())
                .andReturn();
        assertThat(result.getResponse().getContentAsString()).isEqualTo("true");
    }

    @Test
    void user_info() {
    }
}