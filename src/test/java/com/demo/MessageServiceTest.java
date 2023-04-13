package com.demo;

import com.demo.entity.Message;
import com.demo.entity.User;
import com.demo.entity.vo.MessageVo;
import com.demo.repository.MessageRepository;
import com.demo.service.MessageService;
import com.demo.service.MessageVoService;
import com.demo.service.UserService;
import org.junit.Assert;
import org.junit.Before;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.test.context.junit4.SpringRunner;

import java.time.LocalDateTime;
import java.util.Iterator;
import java.util.List;
import java.util.Optional;

@RunWith(SpringRunner.class)
@SpringBootTest
public class MessageServiceTest {
    @Autowired
    private MessageService messageService;

    @Autowired
    private MessageVoService messageVoService;

    @Mock
    private UserService userService;

    @Autowired
    private MessageRepository messageRepository;

    @Before
    public void setup(){
        User user = new User(1, "test_only", "test_only", "1", null, null, 0, null);
        userService.create(user);
    }

    @Test
    void findByUser() {
        String userID = "test_only";
        String content = "unit test create";
        Message message = new Message(1, userID, content, LocalDateTime.now(), 1);
        messageRepository.save(message);
        messageRepository.save(message);
        messageRepository.save(message);
        System.out.println("插入三条留言");

        Integer pageNum = 1;
        Pageable pageable= PageRequest.of(pageNum - 1, 5);
        Page<Message> messagePage = messageService.findByUser(userID, pageable);
        System.out.println(messagePage);
        List<MessageVo> actualMessageList = messageVoService.returnVo(messagePage.getContent());
        Assert.assertEquals(3, actualMessageList.size());

        Iterator<Message> expectedMessageList = messageRepository.findMessageByUserID(userID).iterator();
        for(MessageVo actualMessageVo:actualMessageList) {
            Message expectedMessage = expectedMessageList.next();
            Assert.assertEquals(userID, actualMessageVo.getUserID());
            Assert.assertEquals(expectedMessage.getMessageID(), actualMessageVo.getMessageID());
            Assert.assertEquals(expectedMessage.getContent(), actualMessageVo.getContent());
            messageRepository.deleteById(actualMessageVo.getMessageID());
        }
    }

    @Test
    void create() {
        String userID = "test_only";
        String content = "unit test create";
        Message message = new Message(1, userID, content, LocalDateTime.now(), 1);
        Integer actualMessageID = messageService.create(message);
        Message actualMessage = messageRepository.findById(actualMessageID).get();
        Assert.assertEquals(content, actualMessage.getContent());
        Assert.assertEquals(userID, actualMessage.getUserID());
        messageRepository.deleteById(actualMessageID);
    }


    @Test
    void delById() {
        String userID = "test_only";
        Iterator<Message> expectedMessageList = messageRepository.findMessageByUserID(userID).iterator();
        for (Iterator<Message> it = expectedMessageList; it.hasNext(); ) {
            Message expected = it.next();
            Integer expectedMessageID = expected.getMessageID();
            messageService.delById(expectedMessageID);

            Optional<Message> message = messageRepository.findById(expectedMessageID);
            Assert.assertFalse(message.isPresent());
        }
    }


    @Test
    void confirmMessage() {
        String userID = "test_only";
        String content = "unit test confirmMessage";
        Message message = new Message(1, userID, content, LocalDateTime.now(), 1);
        Integer actualMessageID = messageService.create(message);
        messageService.confirmMessage(actualMessageID);
        Message actualMessage = messageRepository.findById(actualMessageID).get();
        Assert.assertEquals(2, actualMessage.getState());
        messageRepository.deleteById(actualMessageID);
    }

    @Test
    void rejectMessage() {
        String userID = "test_only";
        String content = "unit test rejectMessage";
        Message message = new Message(1, userID, content, LocalDateTime.now(), 1);
        Integer actualMessageID = messageService.create(message);
        messageService.rejectMessage(actualMessageID);
        Message actualMessage = messageRepository.findById(actualMessageID).get();
        Assert.assertEquals(3, actualMessage.getState());
        messageRepository.deleteById(actualMessageID);
    }

    @Test
    void findWaitState() {
        String userID = "test_only";
        String content = "unit test findWaitState";
        Message message = new Message(1, userID, content, LocalDateTime.now(), 1);
        Integer actualWaitMessageID = messageService.create(message);
        message = new Message(1, userID, content, LocalDateTime.now(), 2);
        Integer actualPassMessageID = messageService.create(message);
        Integer pageNum = 1;
        Pageable pageable= PageRequest.of(pageNum - 1, 5);
        Page<Message> messagePage = messageService.findWaitState(pageable);
        Iterator<Message> expectedMessageList = messageRepository.findMessageByUserIDAndState(userID, 1).iterator();
        for (Iterator<Message> it = expectedMessageList; it.hasNext(); ) {
            Message expected = it.next();
            Integer expectedMessageID = expected.getMessageID();
            Assert.assertEquals(expectedMessageID, actualWaitMessageID);
        }
        messageRepository.deleteById(actualWaitMessageID);
        messageRepository.deleteById(actualPassMessageID);
    }

    @Test
    void findPassState() {
        String userID = "test_only";
        String content = "unit test findPassState";
        Message message = new Message(999, userID, content, LocalDateTime.now(), 1);
        Integer actualWaitMessageID = messageService.create(message);
        message = new Message(1, userID, content, LocalDateTime.now(), 2);
        Integer actualPassMessageID = messageService.create(message);
        Integer pageNum = 1;
        Pageable pageable= PageRequest.of(pageNum - 1, 5);
        Page<Message> messagePage = messageService.findPassState(pageable);
        Iterator<Message> expectedMessageList = messageRepository.findMessageByUserIDAndState(userID, 2).iterator();
        for (Iterator<Message> it = expectedMessageList; it.hasNext(); ) {
            Message expected = it.next();
            Integer expectedMessageID = expected.getMessageID();
            Assert.assertEquals(expectedMessageID, actualPassMessageID);
        }
        messageRepository.deleteById(actualWaitMessageID);
        messageRepository.deleteById(actualPassMessageID);
    }
}
