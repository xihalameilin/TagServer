package com.example.demo.dao;

import com.example.demo.HibernateEntity.Message;

import java.util.ArrayList;

public interface MessageDAO {
    void add(Message message);

    void delete(String messageId);

    void modify(Message message);

    ArrayList<Message> getUnread(String userId);

    ArrayList<Message> getReceived(String userId);

    ArrayList<Message> getAll(String userId);

    Message getById(int messageId);
}
