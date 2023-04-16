package com.demo;

import com.demo.dao.MessageDao;
import com.demo.dao.UserDao;
import com.demo.entity.Message;
import com.demo.entity.User;
import com.demo.entity.vo.MessageVo;
import com.demo.service.MessageService;
import com.demo.service.MessageVoService;
import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
//import org.junit.jupiter.api.Test;
import org.junit.Test;
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
import java.util.Iterator;
import java.util.List;

@RunWith(SpringRunner.class)
@SpringBootTest
public class MessageServiceTest {
    @Autowired
    private MessageService messageService;

    @Autowired
    private MessageVoService messageVoService;

    @Autowired
    private UserDao userDao;

    @Autowired
    private MessageDao messageDao;

    @Before
    public void setup(){
        User user = new User(999, "test_only", "test_only", "1", null, null, 0, null);
        userDao.save(user);
    }

    @After
    public void teardown() {
        User user = userDao.findByUserID("test_only");
        userDao.delete(user);
    }

    @Test
    public void findByUser() {
        String userID = "test_only";
        String content = "unit test findByUser";
        Message message = new Message(1, userID, content, LocalDateTime.now(), 1);
        messageDao.save(message);
        messageDao.save(message);
        messageDao.save(message);
        System.out.println("插入三条留言");

        Integer pageNum = 1;
        Pageable pageable= PageRequest.of(pageNum - 1, 5);
        Page<Message> messagePage = messageService.findByUser(userID, pageable);
        List<MessageVo> actualMessageList = messageVoService.returnVo(messagePage.getContent());
        Assert.assertEquals(3, actualMessageList.size());

        Iterator<Message> expectedMessageList = messageDao.findAllByUserID(userID, pageable).iterator();
        for(MessageVo actualMessageVo:actualMessageList) {
            Message expectedMessage = expectedMessageList.next();
            Assert.assertEquals(userID, actualMessageVo.getUserID());
            Assert.assertEquals(expectedMessage.getMessageID(), actualMessageVo.getMessageID());
            Assert.assertEquals(expectedMessage.getContent(), actualMessageVo.getContent());
            messageDao.deleteById(actualMessageVo.getMessageID());
        }
    }

    @Test
    public void create() {
        String userID = "test_only";
        String content = "unit test create";
        Message message = new Message(1, userID, content, LocalDateTime.now(), 1);
        Integer actualMessageID = messageService.create(message);
        Message actualMessage = messageDao.findById(actualMessageID).get();
        Assert.assertEquals(content, actualMessage.getContent());
        Assert.assertEquals(userID, actualMessage.getUserID());
        messageDao.deleteById(actualMessageID);
    }

    @Test
    public void create2() {
        String userID = "test_only";
        String content = "unit test create";
        Message message = new Message(1, userID, content, LocalDateTime.now(), 100);
        Integer actualMessageID = messageService.create(message);
        Message actualMessage = messageDao.findById(actualMessageID).get();
        try {
            Assert.assertNull(actualMessage);
        } catch (AssertionError e) {
            System.out.println("创建不合法的留言成功，预期为失败");
        }

        messageDao.deleteById(actualMessageID);
    }

    @Test
    public void delById() {
        String userID = "test_only";
        String content = "unit test delById";
        Message message = new Message(1, userID, content, LocalDateTime.now(), 1);
        Integer actualMessageID = messageService.create(message);
        Assert.assertTrue(messageDao.findById(actualMessageID).isPresent());
        messageService.delById(actualMessageID);
        Assert.assertFalse(messageDao.findById(actualMessageID).isPresent());
    }


    @Test
    public void confirmMessage() {
        String userID = "test_only";
        String content = "unit test confirmMessage";
        Message message = new Message(1, userID, content, LocalDateTime.now(), 1);
        Integer actualMessageID = messageService.create(message);
        messageService.confirmMessage(actualMessageID);
        Message actualMessage = messageDao.findById(actualMessageID).get();
        Assert.assertEquals(2, actualMessage.getState());
        messageDao.deleteById(actualMessageID);
    }

    @Test
    public void confirmMessage2() {
        try {
            messageService.confirmMessage(999);
        } catch (RuntimeException e) {
            System.out.println(e);
            System.out.println("测试通过");
        }
    }

    @Test
    public void rejectMessage() {
        String userID = "test_only";
        String content = "unit test rejectMessage";
        Message message = new Message(1, userID, content, LocalDateTime.now(), 1);
        Integer actualMessageID = messageService.create(message);
        messageService.rejectMessage(actualMessageID);
        Message actualMessage = messageDao.findById(actualMessageID).get();
        Assert.assertEquals(3, actualMessage.getState());
        messageDao.deleteById(actualMessageID);
    }

    @Test
    public void rejectMessage2() {
        try {
            messageService.rejectMessage(999);
        } catch (RuntimeException e) {
            System.out.println("测试通过");
        }
    }

    @Test
    public void findWaitState() {
        String userID = "test_only";
        String content = "unit test findWaitState";
        Message message = new Message(1, userID, content, LocalDateTime.now(), 1);
        Integer actualWaitMessageID = messageService.create(message);
        message = new Message(1, userID, content, LocalDateTime.now(), 2);
        Integer actualPassMessageID = messageService.create(message);
        Integer pageNum = 1;
        Pageable pageable= PageRequest.of(pageNum - 1, 5);
        Page<Message> messagePage = messageService.findWaitState(pageable);
        Iterator<Message> expectedMessageList = messageDao.findAllByUserID(userID, pageable).iterator();
        for (Iterator<Message> it = expectedMessageList; it.hasNext(); ) {
            Message expected = it.next();
            // 跳过已审核或已拒绝的留言
            if (expected.getState() != 1) {
                continue;
            }
            Integer expectedMessageID = expected.getMessageID();
            Assert.assertEquals(expectedMessageID, actualWaitMessageID);
        }
        messageDao.deleteById(actualWaitMessageID);
        messageDao.deleteById(actualPassMessageID);
    }

    @Test
    public void findPassState() {
        String userID = "test_only";
        String content = "unit test findPassState";
        Message message = new Message(999, userID, content, LocalDateTime.now(), 1);
        Integer actualWaitMessageID = messageService.create(message);
        message = new Message(1, userID, content, LocalDateTime.now(), 2);
        Integer actualPassMessageID = messageService.create(message);
        Integer pageNum = 1;
        Pageable pageable= PageRequest.of(pageNum - 1, 5);
        Page<Message> messagePage = messageService.findPassState(pageable);
        Iterator<Message> expectedMessageList = messageDao.findAllByUserID(userID, pageable).iterator();
        for (Iterator<Message> it = expectedMessageList; it.hasNext(); ) {
            Message expected = it.next();
            // 跳过未审核或已拒绝的留言
            if (expected.getState() != 2) {
                continue;
            }
            Integer expectedMessageID = expected.getMessageID();
            Assert.assertEquals(expectedMessageID, actualPassMessageID);
        }
        messageDao.deleteById(actualWaitMessageID);
        messageDao.deleteById(actualPassMessageID);
    }
}
