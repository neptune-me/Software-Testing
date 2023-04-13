package com.demo.repository;

import com.demo.entity.User;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import javax.transaction.Transactional;

@Repository
public interface UserRepository extends CrudRepository<User, Integer> {
    User findUserByUserID(String userID);

    @Modifying
    @Transactional
    Iterable<User> deleteUserByUserID(String userID);

}
