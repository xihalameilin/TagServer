package com.example.demo.serviceImpl;

import com.example.demo.HibernateEntity.Message;
import com.example.demo.daoImpl.MessageDAOImpl;
import com.example.demo.service.MessageService;
import org.springframework.stereotype.Service;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

@Service
public class MessageServiceImpl implements MessageService {

    @Override
    public void createMessage(String userId, String messageContent, Date messageTime) {
        DateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        new MessageDAOImpl().add(new Message(userId, 0, messageContent, format.format(messageTime)));
    }

    @Override
    public void deleteMessage(String messageId) {
        new MessageDAOImpl().delete(messageId);
    }

    @Override
    public void modify(int messageId, String userId, int flag, String messageContent, Date messageTime) {
        DateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

        Message message = new Message(userId, flag, messageContent, format.format(messageTime));
        message.setMessageId(messageId);

        new MessageDAOImpl().modify(message);
    }

    @Override
    public void markedAsRead(int messageId) {
        Message message = new MessageDAOImpl().getById(messageId);
        message.setFlag(1);
        new MessageDAOImpl().modify(message);
    }

    @Override
    public ArrayList<Message> getUnreadMessage(String userId) {
        return new MessageDAOImpl().getUnread(userId);
    }

    @Override
    public ArrayList<Message> getReadMessage(String userId) {
        return new MessageDAOImpl().getReceived(userId);
    }


}
