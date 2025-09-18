package com.pnam.repositories;

import com.pnam.pojo.ChatMessage;
import java.util.List;

public interface ChatMessageRepository {

    ChatMessage findById(Long id);

    List<ChatMessage> findByThread(Long threadId);

    ChatMessage save(ChatMessage m);

    void delete(Long id);
}
