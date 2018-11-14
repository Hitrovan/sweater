package com.hitrovan.spring1.repository;

import java.util.List;

import org.springframework.data.repository.CrudRepository;
import com.hitrovan.spring1.domain.Message;

public interface MessageRepository extends CrudRepository<Message, Integer> {
    List<Message> findBySenderName(String senderName);
}