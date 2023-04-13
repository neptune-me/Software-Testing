package com.demo.repository;

import com.demo.entity.Message;
import com.demo.entity.User;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface MessageRepository extends CrudRepository<Message, Integer> {
    Iterable<Message> findMessageByUserID(String userId);

    Iterable<Message> findMessageByUserIDAndState(String userId, int state);
}
