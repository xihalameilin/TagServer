package com.example.demo.service;

import com.example.demo.HibernateEntity.Message;
import com.google.gson.JsonArray;

import java.util.ArrayList;
import java.util.Date;

public interface MessageService {
    /*flag：0-未读  1-已读*/
    void createMessage(String userId, String messageContent, Date messageTime);

    void deleteMessage(String messageId);

    /*编辑message*/
    void modify(int messageId, String userId, int flag, String messageContent, Date messageTime);

    /* 标为已读，是modify的特殊情况（简化版）*/
    void markedAsRead(int messageId);

    /*获得未读消息*/
    ArrayList<Message> getUnreadMessage(String userId);

    /*获得已读消息*/
    ArrayList<Message> getReadMessage(String userId);


}
