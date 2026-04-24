package com.mall.service;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.mall.entity.Message;
import com.mall.repository.MessageRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import java.util.List;

@Service
@RequiredArgsConstructor
public class MessageService {
    private final MessageRepository messageRepository;

    public List<Message> getMessageList(Long userId) {
        return messageRepository.selectList(
            new LambdaQueryWrapper<Message>()
                .eq(Message::getUserId, userId)
                .orderByDesc(Message::getCreateTime)
        );
    }

    public List<Message> getUnreadList(Long userId) {
        return messageRepository.selectList(
            new LambdaQueryWrapper<Message>()
                .eq(Message::getUserId, userId)
                .eq(Message::getIsRead, 0)
                .orderByDesc(Message::getCreateTime)
        );
    }

    public int getUnreadCount(Long userId) {
        return messageRepository.selectCount(
            new LambdaQueryWrapper<Message>()
                .eq(Message::getUserId, userId)
                .eq(Message::getIsRead, 0)
        ).intValue();
    }

    public void markAsRead(Long messageId) {
        Message msg = new Message();
        msg.setId(messageId);
        msg.setIsRead(1);
        messageRepository.updateById(msg);
    }

    public void markAllAsRead(Long userId) {
        Message msg = new Message();
        msg.setIsRead(1);
        messageRepository.update(msg,
            new LambdaQueryWrapper<Message>()
                .eq(Message::getUserId, userId)
                .eq(Message::getIsRead, 0)
        );
    }
}
