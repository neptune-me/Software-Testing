package com.demo.controller.admin;

import com.demo.entity.Message;
import com.demo.entity.vo.MessageVo;
import com.demo.service.MessageService;
import com.demo.service.MessageVoService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.test.web.servlet.MockMvc;

import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(AdminMessageController.class)
class AdminMessageControllerTest {
    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private MessageService messageService;

    @MockBean
    private MessageVoService messageVoService;

    @Test
    void message_manage() throws Exception {
        // 创建一个Page对象，用于模拟返回的待审核消息列表
        Page<Message> messagePage = new PageImpl<>(Arrays.asList(new Message(), new Message()));

        // 当MessageService的findWaitState方法被调用时，返回messagePage对象
        when(messageService.findWaitState(any(Pageable.class))).thenReturn(messagePage);

        mockMvc.perform(get("/message_manage"))
                .andExpect(status().isOk())
                .andExpect(view().name("admin/message_manage"))
                .andExpect(model().attribute("total", 1));
    }

    @Test
    void messageList() throws Exception {
        List<Message> messageList = Arrays.asList(new Message(), new Message());
        List<MessageVo> messageVoList = Arrays.asList(new MessageVo(), new MessageVo());

        Page<Message> messagePage = new PageImpl<>(messageList);

        when(messageService.findWaitState(any(Pageable.class))).thenReturn(messagePage);
        when(messageVoService.returnVo(any(List.class))).thenReturn(messageVoList);

        mockMvc.perform(get("/messageList.do?page=1"))
                .andExpect(status().isOk());
    }

    @Test
    void passMessage() throws Exception {
        int messageID = 1;

        mockMvc.perform(post("/passMessage.do")
                        .param("messageID", String.valueOf(messageID)))
                .andExpect(status().isOk())
                .andExpect(content().string("true"));
    }

    @Test
    void rejectMessage() throws Exception {
        int messageID = 1;

        mockMvc.perform(post("/rejectMessage.do")
                        .param("messageID", String.valueOf(messageID)))
                .andExpect(status().isOk())
                .andExpect(content().string("true"));
    }

    @Test
    void delMessage() throws Exception {
        int messageID = 1;

        mockMvc.perform(post("/delMessage.do")
                        .param("messageID", String.valueOf(messageID)))
                .andExpect(status().isOk())
                .andExpect(content().string("true"));
    }
}