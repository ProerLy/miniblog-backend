package com.miniblog.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.miniblog.entity.Message;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import java.util.List;
import java.util.Map;

@Mapper
public interface MessageMapper extends BaseMapper<Message> {
    List<Map<String, Object>> selectConversationList(@Param("userId") Long userId);
    List<Map<String, Object>> selectConversation(@Param("userId") Long userId, @Param("otherId") Long otherId, @Param("limit") int limit);
}
