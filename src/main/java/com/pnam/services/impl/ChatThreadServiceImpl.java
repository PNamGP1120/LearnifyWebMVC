package com.pnam.services.impl;

import com.pnam.pojo.ChatThread;
import com.pnam.repositories.ChatThreadRepository;
import com.pnam.services.ChatThreadService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional
public class ChatThreadServiceImpl implements ChatThreadService {

    @Autowired
    private ChatThreadRepository threadRepo;

    @Override
    public ChatThread getThreadById(Long id) {
        return threadRepo.findById(id);
    }

    @Override
    public List<ChatThread> getThreadsByCourse(Long courseId) {
        return threadRepo.findByCourse(courseId);
    }

    @Override
    public List<ChatThread> getThreadsByUsers(Long userAId, Long userBId) {
        return threadRepo.findByUsers(userAId, userBId);
    }

    @Override
    public ChatThread createThread(ChatThread ct) {
        return threadRepo.save(ct);
    }

    @Override
    public ChatThread updateThread(ChatThread ct) {
        return threadRepo.save(ct);
    }

    @Override
    public void deleteThread(Long id) {
        threadRepo.delete(id);
    }
}
