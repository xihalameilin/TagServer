package com.example.demo.HibernateEntity;

import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import java.util.Objects;

@Entity
public class Message {
    private int messageId;
    private String userId;
    private Integer flag;
    private String messageContent;
    private String messageTime;

    public Message() {
    }

    public Message(String userId, Integer flag, String messageContent, String messageTime) {
        this.userId = userId;
        this.flag = flag;
        this.messageContent = messageContent;
        this.messageTime = messageTime;
    }

    @Id
    @Column(name = "messageId", nullable = false)
    public int getMessageId() {
        return messageId;
    }

    public void setMessageId(int messageId) {
        this.messageId = messageId;
    }

    @Basic
    @Column(name = "userId", nullable = true, length = 20)
    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    @Basic
    @Column(name = "flag", nullable = true)
    public Integer getFlag() {
        return flag;
    }

    public void setFlag(Integer flag) {
        this.flag = flag;
    }

    @Basic
    @Column(name = "messageContent", nullable = true, length = 200)
    public String getMessageContent() {
        return messageContent;
    }

    public void setMessageContent(String messageContent) {
        this.messageContent = messageContent;
    }


    @Basic
    @Column(name = "messageTime", nullable = true, length = 30)
    public String getMessageTime() {
        return messageTime;
    }

    public void setMessageTime(String messageTime) {
        this.messageTime = messageTime;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Message message = (Message) o;
        return messageId == message.messageId &&
                Objects.equals(userId, message.userId) &&
                Objects.equals(flag, message.flag) &&
                Objects.equals(messageContent, message.messageContent) &&
                Objects.equals(messageTime, message.messageTime);
    }

    @Override
    public int hashCode() {

        return Objects.hash(messageId, userId, flag, messageContent,messageTime);
    }
}
