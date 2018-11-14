package com.hitrovan.sweater.repository;

import java.util.List;

import org.springframework.data.repository.CrudRepository;
import com.hitrovan.sweater.domain.Message;

public interface MessageRepository extends CrudRepository<Message, Integer> {
    List<Message> findBySenderName(String senderName);
}