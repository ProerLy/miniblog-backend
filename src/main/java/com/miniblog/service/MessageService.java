package com.miniblog.service;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.miniblog.entity.Message;
import com.miniblog.mapper.MessageMapper;
import com.miniblog.util.Result;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.List;
import java.util.Map;

@Service
public class MessageService {

    @Autowired
    private MessageMapper messageMapper;

    @Transactional
    public Result<Message> send(Long senderId, Long receiverId, String content) {
        Message msg = new Message();
        msg.setSenderId(senderId);
        msg.setReceiverId(receiverId);
        msg.setContent(content);
        msg.setIsRead(0);
        messageMapper.insert(msg);
        return Result.ok(msg);
    }

    public Result<List<Map<String, Object>>> conversation(Long userId, Long otherId, int limit) {
        return Result.ok(messageMapper.selectConversation(userId, otherId, limit));
    }

    public Result<List<Map<String, Object>>> conversationList(Long userId) {
        return Result.ok(messageMapper.selectConversationList(userId));
    }

    @Transactional
    public Result<Void> markAsRead(Long userId, Long senderId) {
        messageMapper.update(null,
            new com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper<Message>()
                .eq(Message::getReceiverId, userId)
                .eq(Message::getSenderId, senderId)
                .eq(Message::getIsRead, 0)
                .set(Message::getIsRead, 1));
        return Result.ok();
    }

    public Result<Long> unreadCount(Long userId) {
        LambdaQueryWrapper<Message> q = new LambdaQueryWrapper<>();
        q.eq(Message::getReceiverId, userId).eq(Message::getIsRead, 0);
        return Result.ok(messageMapper.selectCount(q));
    }
}
