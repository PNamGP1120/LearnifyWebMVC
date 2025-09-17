package com.pnam.services.impl;

import com.pnam.pojo.ChatMessage;
import com.pnam.repositories.ChatMessageRepository;
import com.pnam.services.ChatMessageService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional
public class ChatMessageServiceImpl implements ChatMessageService {

    @Autowired
    private ChatMessageRepository messageRepo;

    @Override
    public ChatMessage getMessageById(Long id) {
        return messageRepo.findById(id);
    }

    @Override
    public List<ChatMessage> getMessagesByThread(Long threadId) {
        return messageRepo.findByThread(threadId);
    }

    @Override
    public ChatMessage createMessage(ChatMessage m) {
        return messageRepo.save(m);
    }

    @Override
    public ChatMessage updateMessage(ChatMessage m) {
        return messageRepo.save(m);
    }

    @Override
    public void deleteMessage(Long id) {
        messageRepo.delete(id);
    }
}
