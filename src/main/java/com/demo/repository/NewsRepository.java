package com.demo.repository;

import com.demo.entity.News;
import com.demo.entity.User;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import javax.transaction.Transactional;

@Repository
public interface NewsRepository extends CrudRepository<News, Integer> {
        News findByNewsID(String userID);

    @Modifying
    @Transactional
    Iterable<News> deleteUserByNewsID(String userID);

}
