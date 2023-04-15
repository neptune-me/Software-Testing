package com.demo;

import com.demo.dao.MessageDao;
import com.demo.dao.UserDao;
import com.demo.entity.Message;
import com.demo.entity.User;
import com.demo.entity.vo.MessageVo;
import com.demo.service.MessageService;
import com.demo.service.MessageVoService;
import org.junit.*;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
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
    private UserDao userDao;

    @Autowired
    private MessageDao messageDao;

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
    public void returnMessageVoByMessageID() {
        String userID = "test_only";
        String content = "unit test returnMessageVoByMessageID";
        Message message = new Message(1, userID, content, LocalDateTime.now(), 1);
        Integer actualMessageID = messageService.create(message);

        MessageVo actualMessageVo = messageVoService.returnMessageVoByMessageID(actualMessageID);
        Assert.assertEquals(content, actualMessageVo.getContent());
        Assert.assertEquals(userID, actualMessageVo.getUserID());
        Assert.assertEquals(1, actualMessageVo.getState());

        messageDao.deleteById(actualMessageID);
    }

    @Test
    public void returnVo() {
        String userID = "test_only";
        String content = "unit test returnVo";
        for (int i = 0; i < 5; i ++) {
            Message message = new Message(1, userID, content, LocalDateTime.now(), 1);
            messageDao.save(message);
        }

        List<Message> messageList = new ArrayList<>();
        Integer pageNum = 1;
        Pageable pageable= PageRequest.of(pageNum - 1, 5);
        Page<Message> messagePage = messageService.findByUser(userID, pageable);
        messageList = messagePage.getContent();

        List<MessageVo> actualMessageVoList = messageVoService.returnVo(messageList);
        Assert.assertEquals(5, actualMessageVoList.size());
        for (int i = 0; i < 5; i ++) {
            MessageVo actualMessageVo = actualMessageVoList.get(i);
            Assert.assertEquals(content, actualMessageVo.getContent());
            Assert.assertEquals(userID, actualMessageVo.getUserID());
            Assert.assertEquals(1, actualMessageVo.getState());
            System.out.println(actualMessageVo.getMessageID());
            messageDao.deleteById(actualMessageVo.getMessageID());
        }

    }

}
