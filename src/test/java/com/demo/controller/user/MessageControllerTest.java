package com.demo.controller.user;

import com.demo.entity.Message;
import com.demo.entity.User;
import com.demo.entity.vo.MessageVo;
import com.demo.service.MessageService;
import com.demo.service.MessageVoService;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.domain.*;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockHttpSession;
import org.springframework.test.web.servlet.MockMvc;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import static org.mockito.ArgumentMatchers.argThat;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(MessageController.class)
class MessageControllerTest {
    // 使用MockMvc模拟HTTP请求
    @Autowired
    private MockMvc mockMvc;

    // 使用Mockito模拟服务层
    @MockBean
    private MessageService messageService;

    @MockBean
    private MessageVoService messageVoService;

    // 模拟Message对象
    @Mock
    private Message message;


    // 测试/message_list的GET请求，应该返回200状态码和message_list视图，并且包含通过审核的留言和用户自己的留言总页
    @Test
    public void testMessageList() throws Exception {
        // 创建一个用户对象
        User user = new User(1,"1","test","test123","test@test.com","123456789",0,"");

        //创建一个模拟的HttpSession对象，设置"user"属性
        MockHttpSession session = new MockHttpSession();
        session.setAttribute("user", user);

        // 创建一个分页对象，每页5条数据，按时间降序排序
        Pageable pageable = PageRequest.of(0, 5, Sort.by("time").descending());

        // 创建一个留言对象列表，模拟通过审核的留言
        List<Message> messageList = new ArrayList<>();
        for (int i = 0; i < 5; i++) {
            Message message = new Message();
            message.setMessageID(i + 1);
            message.setContent("This is a test message " + (i + 1));
            message.setTime(LocalDateTime.now().minusMinutes(i));
            message.setState(1);
            message.setUserID(String.valueOf(i));
            messageList.add(message);
        }

        // 创建一个分页对象，包含留言对象列表和总页数
        Page<Message> messagePage = new PageImpl<>(messageList, pageable, 10);

        // 创建一个留言视图对象列表，模拟转换后的留言视图
        List<MessageVo> messageVoList = new ArrayList<>();
        for (Message message : messageList) {
            MessageVo messageVo = new MessageVo();
            messageVo.setMessageID(message.getMessageID());
            messageVo.setContent(message.getContent());
            messageVo.setTime(message.getTime());
            messageVo.setState(message.getState());
            messageVo.setUserName(message.getUserID());
            messageVoList.add(messageVo);
        }

        // 当调用messageService.findPassState(pageable)时，返回messagePage对象
        when(messageService.findPassState(pageable)).thenReturn(messagePage);

        // 当调用messageVoService.returnVo(messageList)时，返回messageVoList对象
        when(messageVoService.returnVo(messageList)).thenReturn(messageVoList);

        when(messageService.findByUser(user.getUserID(),pageable)).thenReturn(messagePage);

        // 模拟一个已登录的用户，使用SecurityMockMvcRequestPostProcessors工具类
        mockMvc.perform(get("/message_list").session(session))
                .andExpect(status().isOk())
                .andExpect(view().name("message_list"))
                .andExpect(model().attribute("total", 2))
                .andExpect(model().attribute("user_total", 2));
    }



    // 测试/message/getMessageList的GET请求，应该返回200状态码和JSON格式的留言视图对象列表
    @Test
    public void testGetMessageList() throws Exception {
        // 创建一个用户对象
        User user = new User(1,"1","test","test123","test@test.com","123456789",0,"");

        // 创建一个分页对象，每页5条数据，按时间降序排序
        Pageable pageable = PageRequest.of(0, 5, Sort.by("time").descending());

        // 创建一个留言对象列表，模拟通过审核的留言
        List<Message> messageList = new ArrayList<>();
        for (int i = 0; i < 5; i++) {
            Message message = new Message();
            message.setMessageID(i + 1);
            message.setContent("This is a test message " + (i + 1));
            message.setTime(LocalDateTime.now().minusMinutes(i));
            message.setState(1);
            message.setUserID(String.valueOf(i));
            messageList.add(message);
        }

        // 创建一个分页对象，包含留言对象列表和总页数
        Page<Message> messagePage = new PageImpl<>(messageList, pageable, 10);

        // 创建一个留言视图对象列表，模拟转换后的留言视图
        List<MessageVo> messageVoList = new ArrayList<>();
        for (Message message : messageList) {
            MessageVo messageVo = new MessageVo();
            messageVo.setMessageID(message.getMessageID());
            messageVo.setContent(message.getContent());
            messageVo.setTime(message.getTime());
            messageVo.setState(message.getState());
            messageVo.setUserName(message.getUserID());
            messageVoList.add(messageVo);
        }

        // 当调用messageService.findPassState(pageable)时，返回messagePage对象
        when(messageService.findPassState(pageable)).thenReturn(messagePage);

        // 当调用messageVoService.returnVo(messageList)时，返回messageVoList对象
        when(messageVoService.returnVo(messageList)).thenReturn(messageVoList);

        // 模拟一个GET请求，传递page参数为1
        mockMvc.perform(get("/message/getMessageList")
                        .param("page", "1"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$[0].messageID").value(1))
                .andExpect(jsonPath("$[0].content").value("This is a test message 1"))
                .andExpect(jsonPath("$[0].userName").value("0"))
                .andExpect(jsonPath("$[0].state").value(1))
                // 以此类推，比较其他的留言视图对象
                .andExpect(jsonPath("$[1].messageID").value(2));
        // ...
    }


    @Test
    void user_message_list() throws Exception {
        // 创建一个用户对象
        User user = new User(1,"1","test","test123","test@test.com","123456789",0,"");

        //创建一个模拟的HttpSession对象，设置"user"属性
        MockHttpSession session = new MockHttpSession();
        session.setAttribute("user", user);

        // 创建一个分页对象，每页5条数据，按时间降序排序
        Pageable pageable = PageRequest.of(0, 5, Sort.by("time").descending());

        // 创建一个留言对象列表，模拟通过审核的留言
        List<Message> messageList = new ArrayList<>();
        Message message = new Message();
        message.setMessageID(1);
        message.setContent("This is a test message 1");
        message.setTime(LocalDateTime.now().minusMinutes(1));
        messageList.add(message);


        List<MessageVo> messageVoList=new ArrayList<>();
        MessageVo messageVo = new MessageVo();
        messageVo.setMessageID(1);
        messageVo.setContent("This is a test message 1");
        messageVo.setTime(message.getTime());
        messageVoList.add(messageVo);



        // 创建一个分页对象，包含留言对象列表和总页数
        Page<Message> messagePage = new PageImpl<>(messageList, pageable, 10);

        when(messageService.findByUser(user.getUserID(),pageable)).thenReturn(messagePage);
        when(messageVoService.returnVo(messageList)).thenReturn(messageVoList);
        mockMvc.perform(get("/message/findUserList")
                        .param("page", "1").session(session))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$[0].messageID").value(1))
                .andExpect(jsonPath("$[0].content").value("This is a test message 1"));
        // ...
    }

    @Test
    void sendMessage() throws Exception {
        // 构造一个发送消息的请求，指定userID和content参数
        mockMvc.perform(post("/sendMessage")
                        .param("userID", "123")
                        .param("content", "Hello world"))
                // 验证响应是否重定向到/message_list
                .andExpect(redirectedUrl("/message_list"));

        // 验证MessageService的create方法是否被调用了一次，并传入了正确的参数
        verify(messageService).create(argThat(message -> message.getUserID().equals("123") && message.getContent().equals("Hello world")));
    }

    @Test
    void modifyMessage() throws Exception {
        // 设置模拟Message对象的属性
        when(message.getMessageID()).thenReturn(1);
        when(message.getContent()).thenReturn("Hello world");
        when(message.getTime()).thenReturn(LocalDateTime.now());
        when(message.getState()).thenReturn(1);

        // 设置模拟MessageService对象的行为
        when(messageService.findById(1)).thenReturn(message);
        doNothing().when(messageService).update(any(Message.class));

        // 构造一个修改消息的请求，指定messageID和content参数
        mockMvc.perform(post("/modifyMessage.do")
                        .param("messageID", "1")
                        .param("content", "Hello world"))
                // 验证响应是否为true
                .andExpect(content().string("true"));

        // 验证MessageService的findById方法是否被调用了一次，并传入了正确的参数
        verify(messageService).findById(1);

        // 验证MessageService的update方法是否被调用了一次，并传入了正确的参数
        verify(messageService).update(argThat(message -> message.getMessageID() == 1 && message.getContent().equals("Hello world") && message.getState() == 1));
    }

    @Test
    void delMessage() throws Exception {
        // 设置模拟MessageService对象的行为
        doNothing().when(messageService).delById(anyInt());

        // 构造一个删除消息的请求，指定messageID参数
        mockMvc.perform(post("/delMessage.do")
                        .param("messageID", "1"))
                // 验证响应是否为true
                .andExpect(content().string("true"));

        // 验证MessageService的delById方法是否被调用了一次，并传入了正确的参数
        verify(messageService).delById(1);
    }
}