package com.pnam.services;

import com.pnam.pojo.ChatMessage;
import java.util.List;

public interface ChatMessageService {
    ChatMessage getMessageById(Long id);
    List<ChatMessage> getMessagesByThread(Long threadId);
    ChatMessage createMessage(ChatMessage m);
    ChatMessage updateMessage(ChatMessage m);
    void deleteMessage(Long id);
}
