package com.demo;

import com.demo.entity.Message;
import com.demo.entity.User;
import com.demo.entity.vo.MessageVo;
import com.demo.repository.MessageRepository;
import com.demo.service.MessageService;
import com.demo.service.MessageVoService;
import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@RunWith(SpringRunner.class)
@SpringBootTest
public class MessageVoServiceTest {
    @Autowired
    private MessageVoService messageVoService;

    @Autowired
    private MessageService messageService;


    @Autowired
    private MessageRepository messageRepository;

    @Before
    public void setup(){
//        User user = new User(1, "test_only", "test_only", "1", null, null, 0, null);
//        userService.create(user);
    }

    @Test
    public void returnMessageVoByMessageID() {
        String userID = "test_only";
        String content = "unit test returnMessageVoByMessageID";
        Message message = new Message(1, userID, content, LocalDateTime.now(), 1);
        Integer actualMessageID = messageService.create(message);

        MessageVo actualMessageVo = messageVoService.returnMessageVoByMessageID(actualMessageID);
        Assert.assertEquals(content, actualMessageVo.getContent());
        Assert.assertEquals(userID, actualMessageVo.getUserID());
        Assert.assertEquals(1, actualMessageVo.getState());

        messageRepository.deleteById(actualMessageID);
    }

    @Test
    public void returnVo() {
        String userID = "test_only";
        String content = "unit test returnVo";
        List<Message> messageList = new ArrayList<>();
        List<Integer> messageIdList = new ArrayList<>();
        for (int i = 0; i < 5; i ++) {
            Message message = new Message(1, userID, content, LocalDateTime.now(), 1);
            Integer actualMessageID = messageService.create(message);
            message.setMessageID(actualMessageID);
            messageList.add(message);
            messageIdList.add(actualMessageID);
        }

        List<MessageVo> actualMessageVoList = messageVoService.returnVo(messageList);
        for (int i = 0; i < 5; i ++) {
            MessageVo actualMessageVo = actualMessageVoList.get(i);
            Assert.assertEquals(messageIdList.get(i), Integer.valueOf(actualMessageVo.getMessageID()));
            Assert.assertEquals(content, actualMessageVo.getContent());
            Assert.assertEquals(userID, actualMessageVo.getUserID());
            Assert.assertEquals(1, actualMessageVo.getState());
            messageRepository.deleteById(actualMessageVo.getMessageID());
        }

    }

    @After
    public void teardown() {

    }
}
